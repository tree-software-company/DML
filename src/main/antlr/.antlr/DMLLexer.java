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
		T__9=10, T__10=11, T__11=12, TYPE=13, STRING=14, NUMBER=15, BOOLEAN=16, 
		IDENTIFIER=17, WS=18, LINE_COMMENT=19, BLOCK_COMMENT=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "TYPE", "STRING", "NUMBER", "BOOLEAN", "IDENTIFIER", 
			"WS", "LINE_COMMENT", "BLOCK_COMMENT"
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
			null, "TYPE", "STRING", "NUMBER", "BOOLEAN", "IDENTIFIER", "WS", "LINE_COMMENT", 
			"BLOCK_COMMENT"
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
		"\u0004\u0000\u0014\u009f\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\\\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\rb\b\r\n\r\f\re\t\r\u0001\r\u0001\r\u0001\u000e\u0004\u000ej\b\u000e"+
		"\u000b\u000e\f\u000ek\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f"+
		"w\b\u000f\u0001\u0010\u0001\u0010\u0005\u0010{\b\u0010\n\u0010\f\u0010"+
		"~\t\u0010\u0001\u0011\u0004\u0011\u0081\b\u0011\u000b\u0011\f\u0011\u0082"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0005\u0012\u008b\b\u0012\n\u0012\f\u0012\u008e\t\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u0096"+
		"\b\u0013\n\u0013\f\u0013\u0099\t\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0097\u0000\u0014\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012%\u0013\'\u0014\u0001\u0000\u0006\u0002\u0000\"\"\\\\\u0001\u0000"+
		"09\u0003\u0000AZ__az\u0004\u000009AZ__az\u0003\u0000\t\n\r\r  \u0002\u0000"+
		"\n\n\r\r\u00aa\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"+
		"\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"+
		"\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"+
		"\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"+
		"\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"+
		"\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000"+
		"\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000"+
		"\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000"+
		"\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%"+
		"\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0001)\u0001"+
		"\u0000\u0000\u0000\u0003+\u0001\u0000\u0000\u0000\u0005-\u0001\u0000\u0000"+
		"\u0000\u0007/\u0001\u0000\u0000\u0000\t1\u0001\u0000\u0000\u0000\u000b"+
		"3\u0001\u0000\u0000\u0000\r5\u0001\u0000\u0000\u0000\u000f7\u0001\u0000"+
		"\u0000\u0000\u00119\u0001\u0000\u0000\u0000\u0013;\u0001\u0000\u0000\u0000"+
		"\u0015=\u0001\u0000\u0000\u0000\u0017?\u0001\u0000\u0000\u0000\u0019["+
		"\u0001\u0000\u0000\u0000\u001b]\u0001\u0000\u0000\u0000\u001di\u0001\u0000"+
		"\u0000\u0000\u001fv\u0001\u0000\u0000\u0000!x\u0001\u0000\u0000\u0000"+
		"#\u0080\u0001\u0000\u0000\u0000%\u0086\u0001\u0000\u0000\u0000\'\u0091"+
		"\u0001\u0000\u0000\u0000)*\u0005=\u0000\u0000*\u0002\u0001\u0000\u0000"+
		"\u0000+,\u0005;\u0000\u0000,\u0004\u0001\u0000\u0000\u0000-.\u0005+\u0000"+
		"\u0000.\u0006\u0001\u0000\u0000\u0000/0\u0005.\u0000\u00000\b\u0001\u0000"+
		"\u0000\u000012\u0005(\u0000\u00002\n\u0001\u0000\u0000\u000034\u0005)"+
		"\u0000\u00004\f\u0001\u0000\u0000\u000056\u0005[\u0000\u00006\u000e\u0001"+
		"\u0000\u0000\u000078\u0005,\u0000\u00008\u0010\u0001\u0000\u0000\u0000"+
		"9:\u0005]\u0000\u0000:\u0012\u0001\u0000\u0000\u0000;<\u0005{\u0000\u0000"+
		"<\u0014\u0001\u0000\u0000\u0000=>\u0005}\u0000\u0000>\u0016\u0001\u0000"+
		"\u0000\u0000?@\u0005:\u0000\u0000@\u0018\u0001\u0000\u0000\u0000AB\u0005"+
		"s\u0000\u0000BC\u0005t\u0000\u0000CD\u0005r\u0000\u0000DE\u0005i\u0000"+
		"\u0000EF\u0005n\u0000\u0000F\\\u0005g\u0000\u0000GH\u0005n\u0000\u0000"+
		"HI\u0005u\u0000\u0000IJ\u0005m\u0000\u0000JK\u0005b\u0000\u0000KL\u0005"+
		"e\u0000\u0000L\\\u0005r\u0000\u0000MN\u0005b\u0000\u0000NO\u0005o\u0000"+
		"\u0000OP\u0005o\u0000\u0000PQ\u0005l\u0000\u0000QR\u0005e\u0000\u0000"+
		"RS\u0005a\u0000\u0000S\\\u0005n\u0000\u0000TU\u0005l\u0000\u0000UV\u0005"+
		"i\u0000\u0000VW\u0005s\u0000\u0000W\\\u0005t\u0000\u0000XY\u0005m\u0000"+
		"\u0000YZ\u0005a\u0000\u0000Z\\\u0005p\u0000\u0000[A\u0001\u0000\u0000"+
		"\u0000[G\u0001\u0000\u0000\u0000[M\u0001\u0000\u0000\u0000[T\u0001\u0000"+
		"\u0000\u0000[X\u0001\u0000\u0000\u0000\\\u001a\u0001\u0000\u0000\u0000"+
		"]c\u0005\"\u0000\u0000^b\b\u0000\u0000\u0000_`\u0005\\\u0000\u0000`b\t"+
		"\u0000\u0000\u0000a^\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000"+
		"be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000"+
		"\u0000df\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000fg\u0005\"\u0000"+
		"\u0000g\u001c\u0001\u0000\u0000\u0000hj\u0007\u0001\u0000\u0000ih\u0001"+
		"\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000"+
		"kl\u0001\u0000\u0000\u0000l\u001e\u0001\u0000\u0000\u0000mn\u0005t\u0000"+
		"\u0000no\u0005r\u0000\u0000op\u0005u\u0000\u0000pw\u0005e\u0000\u0000"+
		"qr\u0005f\u0000\u0000rs\u0005a\u0000\u0000st\u0005l\u0000\u0000tu\u0005"+
		"s\u0000\u0000uw\u0005e\u0000\u0000vm\u0001\u0000\u0000\u0000vq\u0001\u0000"+
		"\u0000\u0000w \u0001\u0000\u0000\u0000x|\u0007\u0002\u0000\u0000y{\u0007"+
		"\u0003\u0000\u0000zy\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000"+
		"|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\"\u0001\u0000\u0000"+
		"\u0000~|\u0001\u0000\u0000\u0000\u007f\u0081\u0007\u0004\u0000\u0000\u0080"+
		"\u007f\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082"+
		"\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0001\u0000\u0000\u0000\u0084\u0085\u0006\u0011\u0000\u0000\u0085"+
		"$\u0001\u0000\u0000\u0000\u0086\u0087\u0005/\u0000\u0000\u0087\u0088\u0005"+
		"/\u0000\u0000\u0088\u008c\u0001\u0000\u0000\u0000\u0089\u008b\b\u0005"+
		"\u0000\u0000\u008a\u0089\u0001\u0000\u0000\u0000\u008b\u008e\u0001\u0000"+
		"\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000"+
		"\u0000\u0000\u008d\u008f\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000"+
		"\u0000\u0000\u008f\u0090\u0006\u0012\u0000\u0000\u0090&\u0001\u0000\u0000"+
		"\u0000\u0091\u0092\u0005/\u0000\u0000\u0092\u0093\u0005*\u0000\u0000\u0093"+
		"\u0097\u0001\u0000\u0000\u0000\u0094\u0096\t\u0000\u0000\u0000\u0095\u0094"+
		"\u0001\u0000\u0000\u0000\u0096\u0099\u0001\u0000\u0000\u0000\u0097\u0098"+
		"\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0098\u009a"+
		"\u0001\u0000\u0000\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u009a\u009b"+
		"\u0005*\u0000\u0000\u009b\u009c\u0005/\u0000\u0000\u009c\u009d\u0001\u0000"+
		"\u0000\u0000\u009d\u009e\u0006\u0013\u0000\u0000\u009e(\u0001\u0000\u0000"+
		"\u0000\n\u0000[ackv|\u0082\u008c\u0097\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}