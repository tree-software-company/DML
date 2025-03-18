// Generated from /Users/szymonmastalerz/Documents/_prywatne-studia/tree/DML/src/main/antlr/DML.g4 by ANTLR 4.13.1

    package parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class DMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, TYPE=13, STRING=14, NUMBER=15, BOOLEAN=16, 
		IDENTIFIER=17, WS=18;
	public static final int
		RULE_file = 0, RULE_statement = 1, RULE_variableDeclaration = 2, RULE_expression = 3, 
		RULE_additionExpression = 4, RULE_propertyAccessExpression = 5, RULE_primaryExpression = 6, 
		RULE_listExpression = 7, RULE_mapExpression = 8, RULE_pair = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"file", "statement", "variableDeclaration", "expression", "additionExpression", 
			"propertyAccessExpression", "primaryExpression", "listExpression", "mapExpression", 
			"pair"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "';'", "'+'", "'.'", "'('", "')'", "'['", "','", "']'", 
			"'{'", "'}'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "TYPE", "STRING", "NUMBER", "BOOLEAN", "IDENTIFIER", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "DML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FileContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(20);
				statement();
				}
				}
				setState(23); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==TYPE );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			variableDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(DMLParser.TYPE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(DMLParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27);
			match(TYPE);
			setState(28);
			match(IDENTIFIER);
			setState(29);
			match(T__0);
			setState(30);
			expression();
			setState(31);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public AdditionExpressionContext additionExpression() {
			return getRuleContext(AdditionExpressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			additionExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AdditionExpressionContext extends ParserRuleContext {
		public List<PropertyAccessExpressionContext> propertyAccessExpression() {
			return getRuleContexts(PropertyAccessExpressionContext.class);
		}
		public PropertyAccessExpressionContext propertyAccessExpression(int i) {
			return getRuleContext(PropertyAccessExpressionContext.class,i);
		}
		public AdditionExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additionExpression; }
	}

	public final AdditionExpressionContext additionExpression() throws RecognitionException {
		AdditionExpressionContext _localctx = new AdditionExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_additionExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			propertyAccessExpression();
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(36);
				match(T__2);
				setState(37);
				propertyAccessExpression();
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyAccessExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(DMLParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(DMLParser.IDENTIFIER, i);
		}
		public PropertyAccessExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyAccessExpression; }
	}

	public final PropertyAccessExpressionContext propertyAccessExpression() throws RecognitionException {
		PropertyAccessExpressionContext _localctx = new PropertyAccessExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_propertyAccessExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			primaryExpression();
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(44);
				match(T__3);
				setState(45);
				match(IDENTIFIER);
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DMLParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(DMLParser.NUMBER, 0); }
		public TerminalNode BOOLEAN() { return getToken(DMLParser.BOOLEAN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(DMLParser.IDENTIFIER, 0); }
		public ListExpressionContext listExpression() {
			return getRuleContext(ListExpressionContext.class,0);
		}
		public MapExpressionContext mapExpression() {
			return getRuleContext(MapExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_primaryExpression);
		try {
			setState(61);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(51);
				match(STRING);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(52);
				match(NUMBER);
				}
				break;
			case BOOLEAN:
				enterOuterAlt(_localctx, 3);
				{
				setState(53);
				match(BOOLEAN);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(54);
				match(IDENTIFIER);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 5);
				{
				setState(55);
				listExpression();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 6);
				{
				setState(56);
				mapExpression();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 7);
				{
				setState(57);
				match(T__4);
				setState(58);
				expression();
				setState(59);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListExpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ListExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listExpression; }
	}

	public final ListExpressionContext listExpression() throws RecognitionException {
		ListExpressionContext _localctx = new ListExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_listExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(T__6);
			setState(64);
			expression();
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(65);
				match(T__7);
				setState(66);
				expression();
				}
				}
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(72);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MapExpressionContext extends ParserRuleContext {
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public MapExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapExpression; }
	}

	public final MapExpressionContext mapExpression() throws RecognitionException {
		MapExpressionContext _localctx = new MapExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_mapExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(T__9);
			setState(75);
			pair();
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(76);
				match(T__7);
				setState(77);
				pair();
				}
				}
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(83);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DMLParser.STRING, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(STRING);
			setState(86);
			match(T__11);
			setState(87);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0012Z\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0001\u0000\u0004\u0000\u0016\b\u0000\u000b"+
		"\u0000\f\u0000\u0017\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004\'\b\u0004\n\u0004\f\u0004*"+
		"\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005/\b\u0005\n\u0005"+
		"\f\u00052\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006>\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u0007D\b\u0007\n\u0007\f\u0007G\t\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0005\bO\b\b\n\b\f\bR\t\b\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0000\u0000\n\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0000\u0000Z\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0002\u0019\u0001\u0000\u0000\u0000\u0004\u001b\u0001\u0000\u0000\u0000"+
		"\u0006!\u0001\u0000\u0000\u0000\b#\u0001\u0000\u0000\u0000\n+\u0001\u0000"+
		"\u0000\u0000\f=\u0001\u0000\u0000\u0000\u000e?\u0001\u0000\u0000\u0000"+
		"\u0010J\u0001\u0000\u0000\u0000\u0012U\u0001\u0000\u0000\u0000\u0014\u0016"+
		"\u0003\u0002\u0001\u0000\u0015\u0014\u0001\u0000\u0000\u0000\u0016\u0017"+
		"\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0017\u0018"+
		"\u0001\u0000\u0000\u0000\u0018\u0001\u0001\u0000\u0000\u0000\u0019\u001a"+
		"\u0003\u0004\u0002\u0000\u001a\u0003\u0001\u0000\u0000\u0000\u001b\u001c"+
		"\u0005\r\u0000\u0000\u001c\u001d\u0005\u0011\u0000\u0000\u001d\u001e\u0005"+
		"\u0001\u0000\u0000\u001e\u001f\u0003\u0006\u0003\u0000\u001f \u0005\u0002"+
		"\u0000\u0000 \u0005\u0001\u0000\u0000\u0000!\"\u0003\b\u0004\u0000\"\u0007"+
		"\u0001\u0000\u0000\u0000#(\u0003\n\u0005\u0000$%\u0005\u0003\u0000\u0000"+
		"%\'\u0003\n\u0005\u0000&$\u0001\u0000\u0000\u0000\'*\u0001\u0000\u0000"+
		"\u0000(&\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)\t\u0001\u0000"+
		"\u0000\u0000*(\u0001\u0000\u0000\u0000+0\u0003\f\u0006\u0000,-\u0005\u0004"+
		"\u0000\u0000-/\u0005\u0011\u0000\u0000.,\u0001\u0000\u0000\u0000/2\u0001"+
		"\u0000\u0000\u00000.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u0000"+
		"1\u000b\u0001\u0000\u0000\u000020\u0001\u0000\u0000\u00003>\u0005\u000e"+
		"\u0000\u00004>\u0005\u000f\u0000\u00005>\u0005\u0010\u0000\u00006>\u0005"+
		"\u0011\u0000\u00007>\u0003\u000e\u0007\u00008>\u0003\u0010\b\u00009:\u0005"+
		"\u0005\u0000\u0000:;\u0003\u0006\u0003\u0000;<\u0005\u0006\u0000\u0000"+
		"<>\u0001\u0000\u0000\u0000=3\u0001\u0000\u0000\u0000=4\u0001\u0000\u0000"+
		"\u0000=5\u0001\u0000\u0000\u0000=6\u0001\u0000\u0000\u0000=7\u0001\u0000"+
		"\u0000\u0000=8\u0001\u0000\u0000\u0000=9\u0001\u0000\u0000\u0000>\r\u0001"+
		"\u0000\u0000\u0000?@\u0005\u0007\u0000\u0000@E\u0003\u0006\u0003\u0000"+
		"AB\u0005\b\u0000\u0000BD\u0003\u0006\u0003\u0000CA\u0001\u0000\u0000\u0000"+
		"DG\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000"+
		"\u0000FH\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000HI\u0005\t\u0000"+
		"\u0000I\u000f\u0001\u0000\u0000\u0000JK\u0005\n\u0000\u0000KP\u0003\u0012"+
		"\t\u0000LM\u0005\b\u0000\u0000MO\u0003\u0012\t\u0000NL\u0001\u0000\u0000"+
		"\u0000OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000PQ\u0001\u0000"+
		"\u0000\u0000QS\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000ST\u0005"+
		"\u000b\u0000\u0000T\u0011\u0001\u0000\u0000\u0000UV\u0005\u000e\u0000"+
		"\u0000VW\u0005\f\u0000\u0000WX\u0003\u0006\u0003\u0000X\u0013\u0001\u0000"+
		"\u0000\u0000\u0006\u0017(0=EP";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}