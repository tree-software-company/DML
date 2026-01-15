package interpreter

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.collections.mutableSetOf
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLBaseVisitor
import parser.DMLParser
import parser.DMLLexer
import java.time.ZoneId

class DMLExecutor(private val symbolTable: SymbolTable) : DMLBaseVisitor<Any?>() {
    private val importedFiles = mutableSetOf<String>()
    private var currentDirectory: Path = Paths.get(System.getProperty("user.dir"))
    private val classes = mutableMapOf<String, Map<String, String>>()
    private val importStack = mutableListOf<String>()
    private val functions = mutableMapOf<String, DMLParser.FunctionDeclarationContext>()
    private val regexPatterns = mutableMapOf<String, Regex>()

    constructor() : this(SymbolTable())

    override fun visitImportStatement(ctx: DMLParser.ImportStatementContext): Any? {
        val filePath = ctx.STRING().text.removeSurrounding("\"").removeSurrounding("'")
        val absolutePath = resolveImportPath(filePath)
        
        if (importStack.contains(absolutePath)) {
            throw RuntimeException("Circular import detected: ${importStack.joinToString(" -> ")} -> $filePath")
        }
        
        if (importedFiles.contains(absolutePath)) {
            return null
        }
        
        val file = File(absolutePath)
        if (!file.exists()) {
            throw RuntimeException("Import error: File not found: $filePath")
        }
        
        try {
            importedFiles.add(absolutePath)
            importStack.add(absolutePath)
            
            val previousDirectory = currentDirectory
            currentDirectory = file.parentFile.toPath()
            
            val content = file.readText()
            val lexer = DMLLexer(CharStreams.fromString(content))
            val tokens = CommonTokenStream(lexer)
            val parser = DMLParser(tokens)
            val tree = parser.file()
            
            visit(tree)
            
            currentDirectory = previousDirectory
            importStack.removeAt(importStack.size - 1)
        } catch (e: Exception) {
            throw RuntimeException("Import error in file $filePath: ${e.message}", e)
        }
        
        return null
    }

    override fun visitTimezoneDeclaration(ctx: DMLParser.TimezoneDeclarationContext): Any? {
        val name = ctx.IDENTIFIER().text
        val tzString = ctx.STRING().text.removeSurrounding("\"").removeSurrounding("'")
        
        val timezone = try {
            ZoneId.of(tzString)
        } catch (e: Exception) {
            throw RuntimeException("Invalid timezone: $tzString")
        }
        
        symbolTable.setVariable(name, timezone)
        return null
    }

    override fun visitFunctionDeclaration(ctx: DMLParser.FunctionDeclarationContext): Any? {
        val name = ctx.IDENTIFIER().text
        functions[name] = ctx
        return null
    }

    override fun visitFunctionCallStatement(ctx: DMLParser.FunctionCallStatementContext): Any? {
        val name = ctx.IDENTIFIER().text
        val args = ctx.argumentList()?.expression()?.map { visit(it) } ?: emptyList()
        
        val result = callFunction(name, args)
        return result
    }

    override fun visitPrintStatement(ctx: DMLParser.PrintStatementContext): Any? {
        val value = visit(ctx.expression())
        println(value)
        return null
    }

    override fun visitReturnStatement(ctx: DMLParser.ReturnStatementContext): Any? {
        val value = if (ctx.expression() != null) visit(ctx.expression()) else null
        throw ReturnValue(value)
    }

    override fun visitVarDeclaration(ctx: DMLParser.VarDeclarationContext): Any? {
        val name = ctx.IDENTIFIER().text
        val value = visit(ctx.expression())
        symbolTable.setVariable(name, value)
        return null
    }

    private fun callFunction(name: String, args: List<Any?>): Any? {
        val function = functions[name] ?: throw RuntimeException("Function '$name' not found")
        
        try {
            val params = function.parameterList()?.IDENTIFIER() ?: emptyList()
            if (params.size != args.size) {
                throw RuntimeException("Function '$name' expects ${params.size} arguments, got ${args.size}")
            }
            
            val oldValues = mutableMapOf<String, Any?>()
            for (i in params.indices) {
                val paramName = params[i].text
                if (symbolTable.hasVariable(paramName)) {
                    oldValues[paramName] = symbolTable.getVariable(paramName)
                }
                symbolTable.setVariable(paramName, args[i])
            }
            
            for (stmt in function.statement()) {
                visit(stmt)
            }
            
            for (i in params.indices) {
                val paramName = params[i].text
                if (oldValues.containsKey(paramName)) {
                    symbolTable.setVariable(paramName, oldValues[paramName])
                } else {
                    symbolTable.removeVariable(paramName)
                }
            }
            
            return null
        } catch (e: ReturnValue) {
            val params = function.parameterList()?.IDENTIFIER() ?: emptyList()
            for (i in params.indices) {
                val paramName = params[i].text
                symbolTable.removeVariable(paramName)
            }
            return e.value
        }
    }

    override fun visitPrimaryExpression(ctx: DMLParser.PrimaryExpressionContext): Any? {
        if (ctx.STRING() != null) {
            return ctx.STRING().text.removeSurrounding("\"").removeSurrounding("'")
        }
        if (ctx.NUMBER() != null) {
            val numText = ctx.NUMBER().text
            return if (numText.contains('.')) numText.toDouble() else numText.toInt()
        }
        if (ctx.BOOLEAN() != null) {
            return ctx.BOOLEAN().text.toBoolean()
        }
        if (ctx.IDENTIFIER() != null) {
            val name = ctx.IDENTIFIER().text
            
            if (ctx.argumentList() != null) {
                val args = ctx.argumentList().expression().map { visit(it) }
                return callFunction(name, args)
            }
            
            return symbolTable.getVariable(name)
        }
        if (ctx.listExpression() != null) {
            return visit(ctx.listExpression())
        }
        if (ctx.mapExpression() != null) {
            return visit(ctx.mapExpression())
        }
        if (ctx.nowFunction() != null) {
            return visit(ctx.nowFunction())
        }
    
        return if (ctx.expression() != null) visit(ctx.expression()) else super.visitPrimaryExpression(ctx)
    }

    override fun visitDeclaration(ctx: DMLParser.DeclarationContext): Any? {
        val type = ctx.TYPE().text
        val name = ctx.IDENTIFIER().text
        val value = ctx.expression()?.let { visit(it) }
        symbolTable.setVariable(name, value)
        return null
    }

    override fun visitAssignment(ctx: DMLParser.AssignmentContext): Any? {
        val obj = ctx.IDENTIFIER(0).text
        val prop = ctx.IDENTIFIER(1).text
        val value = visit(ctx.expression())
        return null
    }

    override fun visitVariableDeclaration(ctx: DMLParser.VariableDeclarationContext): Any? {
        val type = ctx.TYPE().text
        val name = ctx.IDENTIFIER().text
        val value = visit(ctx.expression())
        symbolTable.setVariable(name, value)
        return null
    }

    override fun visitEnumDeclaration(ctx: DMLParser.EnumDeclarationContext): Any? {
        return null
    }

    override fun visitClassDeclaration(ctx: DMLParser.ClassDeclarationContext): Any? {
        val className = ctx.IDENTIFIER().text
        val fields = mutableMapOf<String, String>()
        ctx.classField().forEach { field ->
            val fieldType = field.TYPE().text
            val fieldName = field.IDENTIFIER().text
            fields[fieldName] = fieldType
        }
        classes[className] = fields
        return null
    }

    override fun visitClassInstanceDeclaration(ctx: DMLParser.ClassInstanceDeclarationContext): Any? {
        return null
    }

    override fun visitAssertStatement(ctx: DMLParser.AssertStatementContext): Any? {
        val result = visit(ctx.expression())
        if (result != true) {
            throw AssertionError("Assertion failed: ${ctx.expression().text}")
        }
        return null
    }

    override fun visitAdditionExpression(ctx: DMLParser.AdditionExpressionContext): Any? {
        val expressions = ctx.multiplicationExpression()
        var result = visit(expressions[0])
        
        var opIndex = 0
        for (i in 1 until expressions.size) {
            val right = visit(expressions[i])

            val operator = when {
                ctx.getChild(2 * i - 1).text == "+" -> "+"
                ctx.getChild(2 * i - 1).text == "-" -> "-"
                else -> "+"
            }
            
            result = when (operator) {
                "+" -> when {
                    result is Number && right is Number -> {
                        if (result is Double || right is Double) {
                            result.toDouble() + right.toDouble()
                        } else {
                            result.toInt() + right.toInt()
                        }
                    }
                    result is String || right is String -> result.toString() + right.toString()
                    else -> error("Invalid addition: $result + $right")
                }
                "-" -> when {
                    result is Number && right is Number -> {
                        if (result is Double || right is Double) {
                            result.toDouble() - right.toDouble()
                        } else {
                            result.toInt() - right.toInt()
                        }
                    }
                    else -> error("Invalid subtraction: $result - $right")
                }
                else -> error("Unknown operator: $operator")
            }
            
            opIndex++
        }
        return result
    }

    override fun visitPropertyAccessExpression(ctx: DMLParser.PropertyAccessExpressionContext): Any? {
        var result = visit(ctx.primaryExpression())
        for (id in ctx.IDENTIFIER()) {
            if (result is Map<*, *>) {
                result = result[id.text] ?: error("Field ${id.text} does not exist in map $result")
            } else {
                error("Cannot access field ${id.text} because $result is not a map")
            }
        }
        return result
    }

    override fun visitListExpression(ctx: DMLParser.ListExpressionContext): List<Any> {
        return ctx.expression()?.map { visit(it) ?: "null" } ?: emptyList()
    }

    override fun visitMapExpression(ctx: DMLParser.MapExpressionContext): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        ctx.pair()?.forEach { pair ->
            val key = pair.STRING().text.removeSurrounding("\"").removeSurrounding("'")
            val value = visit(pair.expression()) ?: "null"
            map[key] = value
        }
        return map
    }

    override fun visitNowFunction(ctx: DMLParser.NowFunctionContext): Any {
        val offsetArg = ctx.STRING()?.text?.removeSurrounding("\"")?.removeSurrounding("'")
        var result = java.time.LocalDateTime.now()
    
        if (offsetArg != null) {
            val match = """([+-]\d+)([dhm])""".toRegex().matchEntire(offsetArg)
                ?: error("Invalid offset format in now(). Use e.g. now(\"+1d\")")
    
            val (amountStr, unit) = match.destructured
            val amount = amountStr.toInt()
    
            result = when (unit) {
                "d" -> result.plusDays(amount.toLong())
                "h" -> result.plusHours(amount.toLong())
                "m" -> result.plusMinutes(amount.toLong())
                else -> error("Unsupported unit '$unit'")
            }
        }
        return result
    }

    override fun visitExpression(ctx: DMLParser.ExpressionContext): Any? {
        return visit(ctx.comparisonExpression())
    }

    override fun visitComparisonExpression(ctx: DMLParser.ComparisonExpressionContext): Any? {
        var result = visit(ctx.additionExpression(0))
        
        val opNode = ctx.COMPARISON_OPERATOR()
        if (opNode == null) {
            return result
        }

        val right = visit(ctx.additionExpression(1))
        val operator = opNode.text

        return when (operator) {
            "==" -> result == right
            "!=" -> result != right
            ">"  -> (result as? Comparable<Any>)?.compareTo(right as Any) ?: 
                    error("Invalid types for comparison with >") > 0
            "<"  -> (result as? Comparable<Any>)?.compareTo(right as Any) ?: 
                    error("Invalid types for comparison with <") < 0
            ">=" -> (result as? Comparable<Any>)?.compareTo(right as Any) ?: 
                    error("Invalid types for comparison with >=") >= 0
            "<=" -> (result as? Comparable<Any>)?.compareTo(right as Any) ?: 
                    error("Invalid types for comparison with <=") <= 0
            else -> error("Unknown comparison operator: $operator")
        }
    }

    override fun visitMultiplicationExpression(ctx: DMLParser.MultiplicationExpressionContext): Any? {
        val expressions = ctx.propertyAccessExpression()
        var result = visit(expressions[0])
        
        var opIndex = 0
        for (i in 1 until expressions.size) {
            val right = visit(expressions[i])

            val operator = when {
                ctx.getChild(2 * i - 1).text == "*" -> "*"
                ctx.getChild(2 * i - 1).text == "/" -> "/"
                ctx.getChild(2 * i - 1).text == "%" -> "%"
                else -> "*"
            }
            
            result = when (operator) {
                "*" -> when {
                    result is Number && right is Number -> {
                        if (result is Double || right is Double) {
                            result.toDouble() * right.toDouble()
                        } else {
                            result.toInt() * right.toInt()
                        }
                    }
                    else -> error("Invalid multiplication: $result * $right")
                }
                "/" -> when {
                    result is Number && right is Number -> {
                        if (right.toDouble() == 0.0) error("Division by zero")
                        if (result is Double || right is Double) {
                            result.toDouble() / right.toDouble()
                        } else {
                            result.toInt() / right.toInt()
                        }
                    }
                    else -> error("Invalid division: $result / $right")
                }
                "%" -> when {
                    result is Number && right is Number -> {
                        if (right.toInt() == 0) error("Modulo by zero")
                        result.toInt() % right.toInt()
                    }
                    else -> error("Invalid modulo: $result % $right")
                }
                else -> error("Unknown operator: $operator")
            }
        }
        return result
    }

    fun execute(tree: ParseTree) {
        visit(tree)
    }

    fun executeAndPrint(tree: ParseTree) {
        visit(tree)
        symbolTable.printVariables()
    }

    fun getSymbolTable(): Map<String, Any?> {
        return symbolTable.getAll()
    }
    
    fun getAllRaw(): Map<String, Any?> {
        return symbolTable.getAllRaw()
    }   
    
    private fun isValidUrl(url: String): Boolean {
        val regex = Regex("""^(https?://)?[\w.-]+\.[a-z]{2,}(/.*)?$""", RegexOption.IGNORE_CASE)
        return regex.matches(url)
    }    

    fun executeFile(file: File) {
        val previousDirectory = currentDirectory
        currentDirectory = file.absoluteFile.parentFile.toPath()
        
        val content = file.readText()
        val lexer = DMLLexer(CharStreams.fromString(content))
        val tokens = CommonTokenStream(lexer)
        val parser = DMLParser(tokens)
        val tree = parser.file()
        
        visit(tree)
        
        currentDirectory = previousDirectory
    }
    
    private fun resolveImportPath(importPath: String): String {
        val importFile = File(importPath)

        if (importFile.isAbsolute) {
            return importFile.absolutePath
        }

        val resolvedFile = currentDirectory.resolve(importPath).toFile()
        return resolvedFile.absolutePath
    }

    override fun visitValidation(ctx: DMLParser.ValidationContext): Any? {
        val identifiers = ctx.IDENTIFIER()
        return when {
            identifiers.size == 1 -> {
                val patternName = identifiers[0].text
                val stringNode = ctx.STRING()
                val patternString = stringNode.text
                    .removeSurrounding("\"").removeSurrounding("'")
                    .replace("\\\\", "\\")
                regexPatterns[patternName] = Regex(patternString)
                null
            }
            identifiers.size == 2 -> {
                val variableName = identifiers[0].text
                val patternName = identifiers[1].text
                val value = symbolTable.getVariable(variableName) as? String
                val pattern = regexPatterns[patternName]
                
                val result = if (pattern != null && value != null) {
                    pattern.matches(value)
                } else {
                    false
                }
                
                result
            }
            else -> false
        }
    }

    override fun visitStatement(ctx: DMLParser.StatementContext): Any? {
        return when {
            ctx.declaration() != null -> visitDeclaration(ctx.declaration())
            ctx.assignment() != null -> visitAssignment(ctx.assignment())
            ctx.validation() != null -> visitValidation(ctx.validation())
            ctx.functionCallStatement() != null -> visitFunctionCallStatement(ctx.functionCallStatement())
            ctx.functionDeclaration() != null -> visitFunctionDeclaration(ctx.functionDeclaration())
            ctx.importStatement() != null -> visitImportStatement(ctx.importStatement())
            ctx.printStatement() != null -> visitPrintStatement(ctx.printStatement())
            ctx.returnStatement() != null -> visitReturnStatement(ctx.returnStatement())
            ctx.varDeclaration() != null -> visitVarDeclaration(ctx.varDeclaration())
            ctx.variableDeclaration() != null -> visitVariableDeclaration(ctx.variableDeclaration())
            ctx.enumDeclaration() != null -> visitEnumDeclaration(ctx.enumDeclaration())
            ctx.classDeclaration() != null -> visitClassDeclaration(ctx.classDeclaration())
            ctx.classInstanceDeclaration() != null -> visitClassInstanceDeclaration(ctx.classInstanceDeclaration())
            ctx.assertStatement() != null -> visitAssertStatement(ctx.assertStatement())
            ctx.timezoneDeclaration() != null -> visitTimezoneDeclaration(ctx.timezoneDeclaration())
            else -> null
        }
    }
}

class ReturnValue(val value: Any?) : Exception()
