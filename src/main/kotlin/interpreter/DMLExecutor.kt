package interpreter

import org.antlr.v4.runtime.tree.ParseTree
import parser.DMLBaseVisitor
import parser.DMLParser

class DMLExecutor : DMLBaseVisitor<Any?>() {
    private val symbolTable = SymbolTable()

    override fun visitVariableDeclaration(ctx: DMLParser.VariableDeclarationContext): Any? {
        val type = ctx.TYPE().text
        val name = ctx.IDENTIFIER().text
        val value = visit(ctx.expression())

        when (type) {
            "string" -> symbolTable.setVariable(name, value.toString())
            "number" -> symbolTable.setVariable(name, (value as? Number)?.toInt() ?: 0)
            "boolean" -> symbolTable.setVariable(name, value.toString().toBoolean())
            else -> error("Nieznany typ: $type")
        }

        return null
    }

    override fun visitExpression(ctx: DMLParser.ExpressionContext): Any? {
        if (ctx.STRING() != null) {
            return ctx.STRING().text.removeSurrounding("\"")  // Usunięcie cudzysłowów
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
        if (ctx.children.size == 3 && ctx.getChild(1).text == "+") {
            val left = visit(ctx.getChild(0)) as? Number ?: 0
            val right = visit(ctx.getChild(2)) as? Number ?: 0
            return left.toInt() + right.toInt()
        }
        return super.visitExpression(ctx)
    }

    fun execute(tree: ParseTree) {
        visit(tree)
        symbolTable.printVariables()
    }
}
