package interpreter

import org.antlr.v4.runtime.tree.ParseTree
import parser.DMLBaseVisitor
import parser.DMLParser

class DMLExecutor : DMLBaseVisitor<Any?>() {
    private val symbolTable = SymbolTable()

    private val classes = mutableMapOf<String, Map<String, String>>()

    override fun visitVariableDeclaration(ctx: DMLParser.VariableDeclarationContext): Any? {
        val type = ctx.TYPE().text
        val name = ctx.IDENTIFIER().text
        val isPrivate = ctx.modifier() != null
        val value = visit(ctx.expression()) ?: error("Runtime Error: Variable '$name' has no value.")
    
        val parsedValue = when (type) {
            "string" -> value.toString()
            "number" -> when (value) {
                is Number -> value
                is String -> value.toIntOrNull() ?: value.toDoubleOrNull()
                    ?: error("Value '$value' is not a valid number.")
                else -> error("Type Error: '$name' must be a number, got '${value::class.simpleName}'.")
            }
            "boolean" -> if (value is Boolean) value else error("Type Error: '$name' must be boolean.")
            "list" -> if (value is List<*>) value else error("Type Error: '$name' must be a list.")
            "map" -> if (value is Map<*, *>) value else error("Type Error: '$name' must be a map.")
            "url" -> {
                if (value !is String) {
                    error("Type Error: Variable '$name' must be a valid URL string.")
                }
                if (!isValidUrl(value)) {
                    error("Validation Error: '$value' is not a valid URL.")
                }

                symbolTable.setVariable(name, value)
                value
            }
            "file" -> {
                if (value !is String) {
                    error("Type Error: Variable '$name' must be a valid file path string.")
                }
                val file = java.io.File(value)
                if (!file.exists()) {
                    println("⚠️ Warning: File '$value' does not exist.")
                }
                symbolTable.setVariable(name, value)
                value
            }
            "char" -> {
                val str = value.toString()
                if (str.length != 1) {
                    error("Type Error: Variable '$name' must be a single character.")
                }
                symbolTable.setVariable(name, str)
                str
            }
            "date" -> {
                val parsed = when (value) {
                    is String -> java.time.LocalDate.parse(value)
                    is java.time.LocalDate -> value
                    is java.time.LocalDateTime -> value.toLocalDate()
                    else -> error("Type Error: Variable '$name' must be a date string in format yyyy-MM-dd.")
                }

                symbolTable.setVariable(name, parsed.toString())
                return parsed.toString()
            }
            "datetime" -> {
                val str = when (value) {
                    is String -> value
                    is java.time.LocalDateTime -> value.toString()
                    else -> error("Type Error: Variable '$name' must be a datetime string in format yyyy-MM-dd'T'HH:mm.")
                }

                try {
                    val parsed = java.time.LocalDateTime.parse(str)
                    symbolTable.setVariable(name, parsed.toString())
                    parsed.toString()
                } catch (e: Exception) {
                    error("Invalid datetime format for variable '$name'. Expected yyyy-MM-dd'T'HH:mm.")
                }
            }
            "time" -> {
                val str = when (value) {
                    is String -> value
                    is java.time.LocalDateTime -> value.toLocalTime().toString()
                    is java.time.LocalTime -> value.toString()
                    else -> error("Type Error: Variable '$name' must be a time string in format HH:mm.")
                }

                try {
                    val parsed = java.time.LocalTime.parse(str)
                    symbolTable.setVariable(name, parsed.toString())
                    parsed.toString()
                } catch (e: Exception) {
                    error("Invalid time format for variable '$name'. Expected HH:mm.")
                }
            }

            else -> error("Syntax Error: Unknown type '$type' for variable '$name'.")
        }
    
        symbolTable.setVariable(name, parsedValue, isPrivate)
        return null
    }
    

    override fun visitAdditionExpression(ctx: DMLParser.AdditionExpressionContext): Any? {
        val expressions = ctx.propertyAccessExpression()
        var result = visit(expressions[0])
        for (expr in expressions.drop(1)) {
            val right = visit(expr)
            result = when {
                result is Number && right is Number -> result.toInt() + right.toInt()
                result is String || right is String -> result.toString() + right.toString()
                else -> error("Invalid addition: $result + $right")
            }
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

    override fun visitPrimaryExpression(ctx: DMLParser.PrimaryExpressionContext): Any? {
    
        if (ctx.STRING() != null) {
            return ctx.STRING().text.removeSurrounding("\"")
        }
        if (ctx.NUMBER() != null) {
            val numText = ctx.NUMBER().text
            return if (numText.contains('.')) numText.toDouble() else numText.toInt()
        }
        if (ctx.BOOLEAN() != null) {
            return ctx.BOOLEAN().text.toBoolean()
        }
        if (ctx.IDENTIFIER() != null) {
            return symbolTable.getVariable(ctx.IDENTIFIER().text)
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

    override fun visitListExpression(ctx: DMLParser.ListExpressionContext): List<Any> {
        return ctx.expression().map { visit(it) ?: "null" }
    }

    override fun visitMapExpression(ctx: DMLParser.MapExpressionContext): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        for (pair in ctx.pair()) {
            val key = pair.STRING().text.removeSurrounding("\"")
            val value = visit(pair.expression()) ?: "null"
            map[key] = value
        }
        return map
    }

    override fun visitNowFunction(ctx: DMLParser.NowFunctionContext): Any {
        val offsetArg = ctx.STRING()?.text?.removeSurrounding("\"")
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
    

    fun execute(tree: ParseTree) {
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

    override fun visitEnumDeclaration(ctx: DMLParser.EnumDeclarationContext): Any? {
        val name = ctx.IDENTIFIER(0).text
        val values = mutableMapOf<String, String>()
        
        for (i in 1 until ctx.IDENTIFIER().size) {
            val label = ctx.IDENTIFIER(i).text
            values[label] = label
        }
        
    
        symbolTable.setVariable(name, values)
        return null
    }
    
    override fun visitClassDeclaration(ctx: DMLParser.ClassDeclarationContext): Any? {
        val className = ctx.IDENTIFIER().text
        val fields = mutableMapOf<String, String>()
    
        for (field in ctx.classField()) {
            val type = field.TYPE().text
            val fieldName = field.IDENTIFIER().text
            fields[fieldName] = type
        }
    
        classes[className] = fields
        return null
    }    

    override fun visitClassInstanceDeclaration(ctx: DMLParser.ClassInstanceDeclarationContext): Any? {
        val className = ctx.IDENTIFIER(0).text
        val instanceName = ctx.IDENTIFIER(1).text
    
        val classDefinition = classes[className]
            ?: error("Class '$className' is not defined")
    
        val instance = mutableMapOf<String, Any?>()
    
        for (assign in ctx.classAssignment()) {
            val fieldName = assign.IDENTIFIER().text
            val value = visit(assign.expression())
    
            val expectedType = classDefinition[fieldName]
                ?: error("Field '$fieldName' not declared in class '$className'")
    
            val validatedValue = validateType(expectedType, fieldName, value)
            instance[fieldName] = validatedValue
        }
    
        symbolTable.setVariable(instanceName, instance)
        return null
    }  
    
    override fun visitAssignment(ctx: DMLParser.AssignmentContext): Any? {
        val instanceName = ctx.IDENTIFIER(0).text
        val fieldName = ctx.IDENTIFIER(1).text
    
        val variable = symbolTable.getVariable(instanceName)
            ?: error("Variable '$instanceName' not found")
    
        if (variable !is MutableMap<*, *>) {
            error("Variable '$instanceName' is not an instance")
        }
    
        val value = visit(ctx.expression())
        (variable as MutableMap<String, Any?>)[fieldName] = value
    
        return null
    }    

    private fun validateType(expected: String, fieldName: String, value: Any?): Any? {
        return when (expected) {
            "string" -> if (value is String) value else error("Expected string for '$fieldName'")
            "number" -> when (value) {
                is Int, is Double -> value
                is String -> value.toIntOrNull() ?: value.toDoubleOrNull()
                    ?: error("Expected number for '$fieldName'")
                else -> error("Expected number for '$fieldName'")
            }
            "boolean" -> if (value is Boolean) value else error("Expected boolean for '$fieldName'")
            else -> error("Unknown type '$expected'")
        }
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

    override fun visitAssertStatement(ctx: DMLParser.AssertStatementContext): Any? {
        val result = visit(ctx.expression())
    
        if (result !is Boolean) {
            error("Assert statement requires a boolean expression, got: $result")
        }
    
        if (!result) {
            error("Assertion failed: ${ctx.expression().text}")
        }
    
        println("✅ Assert passed: ${ctx.expression().text}")
        return null
    }
}
