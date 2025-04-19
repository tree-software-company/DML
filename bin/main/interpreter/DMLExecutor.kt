package interpreter

import org.antlr.v4.runtime.tree.ParseTree
import parser.DMLBaseVisitor
import parser.DMLParser

class DMLExecutor : DMLBaseVisitor<Any?>() {
    private val symbolTable = SymbolTable()

    override fun visitVariableDeclaration(ctx: DMLParser.VariableDeclarationContext): Any? {
        val type = ctx.TYPE().text
        val name = ctx.IDENTIFIER().text
        val value = visit(ctx.expression()) ?: error("Runtime Error: Variable '$name' has no value.")

        when (type) {
            "string" -> symbolTable.setVariable(name, value.toString())
            "number" -> {
                val parsed = when (value) {
                    is Number -> value
                    is String -> {
                        value.toIntOrNull() ?: value.toDoubleOrNull()
                        ?: error("Value '$value' is not a valid number.")
                    }
                    else -> error("Type Error: Variable '$name' must be a number, got '${value::class.simpleName}'.")
                }
                symbolTable.setVariable(name, parsed)
            }
            "boolean" -> {
                if (value is Boolean) {
                    symbolTable.setVariable(name, value)
                } else {
                    error("Type Error: Variable '$name' must be a boolean, got '${value::class.simpleName}'.")
                }
            }
            "list" -> {
                if (value is List<*>) {
                    symbolTable.setVariable(name, value)
                } else {
                    error("Type Error: Variable '$name' must be a list, got '${value::class.simpleName}'.")
                }
            }
            "map" -> {
                if (value is Map<*, *>) {
                    symbolTable.setVariable(name, value)
                } else {
                    error("Type Error: Variable '$name' must be a map, got '${value::class.simpleName}'.")
                }
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

        return null
    }

    override fun visitExpression(ctx: DMLParser.ExpressionContext): Any? {
        return visit(ctx.additionExpression())
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

        if (ctx.text.startsWith("now(")) {
            val text = ctx.text
            val regex = """now\("(.*?)"\)""".toRegex()
            val offset = regex.find(text)?.groupValues?.get(1)
    
            var result = java.time.LocalDateTime.now()
    
            if (offset != null) {
    
                val match = """([+-]\d+)([dhm])""".toRegex().matchEntire(offset)
                    ?: error("Invalid offset format in now(). Use e.g. now(\"+1d\"), now(\"-2h\")")
    
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
    
}
