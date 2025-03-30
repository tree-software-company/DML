// Generated from /Users/szymonmastalerz/Documents/_prywatne-studia/tree/DML/src/main/antlr/DML.g4 by ANTLR 4.13.1

    package parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class DMLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, LINE_COMMENT=13, BLOCK_COMMENT=14, TYPE=15, 
		STRING=16, NUMBER=17, BOOLEAN=18, IDENTIFIER=19, WS=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "LINE_COMMENT", "BLOCK_COMMENT", "TYPE", "STRING", 
			"NUMBER", "BOOLEAN", "IDENTIFIER", "WS"
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
			null, "LINE_COMMENT", "BLOCK_COMMENT", "TYPE", "STRING", "NUMBER", "BOOLEAN", 
			"IDENTIFIER", "WS"
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


	public DMLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0014\u009d\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\fF\b"+
		"\f\n\f\f\fI\t\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\rQ\b\r\n\r\f\rT\t\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0003\u000eu\b\u000e\u0001\u000f\u0001\u000f\u0005\u000f"+
		"y\b\u000f\n\u000f\f\u000f|\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0004\u0010\u0081\b\u0010\u000b\u0010\f\u0010\u0082\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0003\u0011\u008e\b\u0011\u0001\u0012\u0001\u0012\u0005"+
		"\u0012\u0092\b\u0012\n\u0012\f\u0012\u0095\t\u0012\u0001\u0013\u0004\u0013"+
		"\u0098\b\u0013\u000b\u0013\f\u0013\u0099\u0001\u0013\u0001\u0013\u0002"+
		"Rz\u0000\u0014\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005"+
		"\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019"+
		"\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014\u0001"+
		"\u0000\u0005\u0002\u0000\n\n\r\r\u0001\u000009\u0003\u0000AZ__az\u0004"+
		"\u000009AZ__az\u0003\u0000\t\n\r\r  \u00a7\u0000\u0001\u0001\u0000\u0000"+
		"\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000"+
		"\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000"+
		"\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000"+
		"\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000"+
		"\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000"+
		"\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000"+
		"\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000"+
		"\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001"+
		"\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000"+
		"\u0000\u0000\u0001)\u0001\u0000\u0000\u0000\u0003+\u0001\u0000\u0000\u0000"+
		"\u0005-\u0001\u0000\u0000\u0000\u0007/\u0001\u0000\u0000\u0000\t1\u0001"+
		"\u0000\u0000\u0000\u000b3\u0001\u0000\u0000\u0000\r5\u0001\u0000\u0000"+
		"\u0000\u000f7\u0001\u0000\u0000\u0000\u00119\u0001\u0000\u0000\u0000\u0013"+
		";\u0001\u0000\u0000\u0000\u0015=\u0001\u0000\u0000\u0000\u0017?\u0001"+
		"\u0000\u0000\u0000\u0019A\u0001\u0000\u0000\u0000\u001bL\u0001\u0000\u0000"+
		"\u0000\u001dt\u0001\u0000\u0000\u0000\u001fv\u0001\u0000\u0000\u0000!"+
		"\u0080\u0001\u0000\u0000\u0000#\u008d\u0001\u0000\u0000\u0000%\u008f\u0001"+
		"\u0000\u0000\u0000\'\u0097\u0001\u0000\u0000\u0000)*\u0005=\u0000\u0000"+
		"*\u0002\u0001\u0000\u0000\u0000+,\u0005;\u0000\u0000,\u0004\u0001\u0000"+
		"\u0000\u0000-.\u0005+\u0000\u0000.\u0006\u0001\u0000\u0000\u0000/0\u0005"+
		".\u0000\u00000\b\u0001\u0000\u0000\u000012\u0005(\u0000\u00002\n\u0001"+
		"\u0000\u0000\u000034\u0005)\u0000\u00004\f\u0001\u0000\u0000\u000056\u0005"+
		"[\u0000\u00006\u000e\u0001\u0000\u0000\u000078\u0005,\u0000\u00008\u0010"+
		"\u0001\u0000\u0000\u00009:\u0005]\u0000\u0000:\u0012\u0001\u0000\u0000"+
		"\u0000;<\u0005{\u0000\u0000<\u0014\u0001\u0000\u0000\u0000=>\u0005}\u0000"+
		"\u0000>\u0016\u0001\u0000\u0000\u0000?@\u0005:\u0000\u0000@\u0018\u0001"+
		"\u0000\u0000\u0000AB\u0005/\u0000\u0000BC\u0005/\u0000\u0000CG\u0001\u0000"+
		"\u0000\u0000DF\b\u0000\u0000\u0000ED\u0001\u0000\u0000\u0000FI\u0001\u0000"+
		"\u0000\u0000GE\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HJ\u0001"+
		"\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000JK\u0006\f\u0000\u0000K\u001a"+
		"\u0001\u0000\u0000\u0000LM\u0005/\u0000\u0000MN\u0005*\u0000\u0000NR\u0001"+
		"\u0000\u0000\u0000OQ\t\u0000\u0000\u0000PO\u0001\u0000\u0000\u0000QT\u0001"+
		"\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000"+
		"SU\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000\u0000UV\u0005*\u0000\u0000"+
		"VW\u0005/\u0000\u0000WX\u0001\u0000\u0000\u0000XY\u0006\r\u0000\u0000"+
		"Y\u001c\u0001\u0000\u0000\u0000Z[\u0005s\u0000\u0000[\\\u0005t\u0000\u0000"+
		"\\]\u0005r\u0000\u0000]^\u0005i\u0000\u0000^_\u0005n\u0000\u0000_u\u0005"+
		"g\u0000\u0000`a\u0005n\u0000\u0000ab\u0005u\u0000\u0000bc\u0005m\u0000"+
		"\u0000cd\u0005b\u0000\u0000de\u0005e\u0000\u0000eu\u0005r\u0000\u0000"+
		"fg\u0005b\u0000\u0000gh\u0005o\u0000\u0000hi\u0005o\u0000\u0000ij\u0005"+
		"l\u0000\u0000jk\u0005e\u0000\u0000kl\u0005a\u0000\u0000lu\u0005n\u0000"+
		"\u0000mn\u0005l\u0000\u0000no\u0005i\u0000\u0000op\u0005s\u0000\u0000"+
		"pu\u0005t\u0000\u0000qr\u0005m\u0000\u0000rs\u0005a\u0000\u0000su\u0005"+
		"p\u0000\u0000tZ\u0001\u0000\u0000\u0000t`\u0001\u0000\u0000\u0000tf\u0001"+
		"\u0000\u0000\u0000tm\u0001\u0000\u0000\u0000tq\u0001\u0000\u0000\u0000"+
		"u\u001e\u0001\u0000\u0000\u0000vz\u0005\"\u0000\u0000wy\t\u0000\u0000"+
		"\u0000xw\u0001\u0000\u0000\u0000y|\u0001\u0000\u0000\u0000z{\u0001\u0000"+
		"\u0000\u0000zx\u0001\u0000\u0000\u0000{}\u0001\u0000\u0000\u0000|z\u0001"+
		"\u0000\u0000\u0000}~\u0005\"\u0000\u0000~ \u0001\u0000\u0000\u0000\u007f"+
		"\u0081\u0007\u0001\u0000\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0081"+
		"\u0082\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082"+
		"\u0083\u0001\u0000\u0000\u0000\u0083\"\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0005t\u0000\u0000\u0085\u0086\u0005r\u0000\u0000\u0086\u0087\u0005u"+
		"\u0000\u0000\u0087\u008e\u0005e\u0000\u0000\u0088\u0089\u0005f\u0000\u0000"+
		"\u0089\u008a\u0005a\u0000\u0000\u008a\u008b\u0005l\u0000\u0000\u008b\u008c"+
		"\u0005s\u0000\u0000\u008c\u008e\u0005e\u0000\u0000\u008d\u0084\u0001\u0000"+
		"\u0000\u0000\u008d\u0088\u0001\u0000\u0000\u0000\u008e$\u0001\u0000\u0000"+
		"\u0000\u008f\u0093\u0007\u0002\u0000\u0000\u0090\u0092\u0007\u0003\u0000"+
		"\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092\u0095\u0001\u0000\u0000"+
		"\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000"+
		"\u0000\u0094&\u0001\u0000\u0000\u0000\u0095\u0093\u0001\u0000\u0000\u0000"+
		"\u0096\u0098\u0007\u0004\u0000\u0000\u0097\u0096\u0001\u0000\u0000\u0000"+
		"\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u0097\u0001\u0000\u0000\u0000"+
		"\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000"+
		"\u009b\u009c\u0006\u0013\u0000\u0000\u009c(\u0001\u0000\u0000\u0000\t"+
		"\u0000GRtz\u0082\u008d\u0093\u0099\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}