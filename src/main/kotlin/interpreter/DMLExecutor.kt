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
                if (value is Number) {
                    symbolTable.setVariable(name, value.toInt())
                } else {
                    error("Type Error: Variable '$name' must be a number, got '${value::class.simpleName}'.")
                }
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
            return ctx.NUMBER().text.toInt()
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

    fun execute(tree: ParseTree) {
        visit(tree)
        symbolTable.printVariables()
    }
}
