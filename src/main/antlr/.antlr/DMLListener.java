package parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DMLParser}.
 */
public interface DMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DMLParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(DMLParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(DMLParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(DMLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(DMLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(DMLParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(DMLParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DMLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DMLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditionExpression(DMLParser.AdditionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#additionExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditionExpression(DMLParser.AdditionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#propertyAccessExpression}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAccessExpression(DMLParser.PropertyAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#propertyAccessExpression}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAccessExpression(DMLParser.PropertyAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(DMLParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(DMLParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#listExpression}.
	 * @param ctx the parse tree
	 */
	void enterListExpression(DMLParser.ListExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#listExpression}.
	 * @param ctx the parse tree
	 */
	void exitListExpression(DMLParser.ListExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#mapExpression}.
	 * @param ctx the parse tree
	 */
	void enterMapExpression(DMLParser.MapExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#mapExpression}.
	 * @param ctx the parse tree
	 */
	void exitMapExpression(DMLParser.MapExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DMLParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(DMLParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link DMLParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(DMLParser.PairContext ctx);
}