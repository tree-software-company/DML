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
		IDENTIFIER=17, WS=18;
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
			"WS"
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
		"\u0004\u0000\u0012\u0080\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\fX\b\f\u0001\r\u0001"+
		"\r\u0005\r\\\b\r\n\r\f\r_\t\r\u0001\r\u0001\r\u0001\u000e\u0004\u000e"+
		"d\b\u000e\u000b\u000e\f\u000ee\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003"+
		"\u000fq\b\u000f\u0001\u0010\u0001\u0010\u0005\u0010u\b\u0010\n\u0010\f"+
		"\u0010x\t\u0010\u0001\u0011\u0004\u0011{\b\u0011\u000b\u0011\f\u0011|"+
		"\u0001\u0011\u0001\u0011\u0001]\u0000\u0012\u0001\u0001\u0003\u0002\u0005"+
		"\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n"+
		"\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012\u0001\u0000\u0004\u0001\u000009\u0003\u0000AZ__az\u0004\u0000"+
		"09AZ__az\u0003\u0000\t\n\r\r  \u0088\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0001%\u0001\u0000\u0000\u0000\u0003\'\u0001\u0000\u0000"+
		"\u0000\u0005)\u0001\u0000\u0000\u0000\u0007+\u0001\u0000\u0000\u0000\t"+
		"-\u0001\u0000\u0000\u0000\u000b/\u0001\u0000\u0000\u0000\r1\u0001\u0000"+
		"\u0000\u0000\u000f3\u0001\u0000\u0000\u0000\u00115\u0001\u0000\u0000\u0000"+
		"\u00137\u0001\u0000\u0000\u0000\u00159\u0001\u0000\u0000\u0000\u0017;"+
		"\u0001\u0000\u0000\u0000\u0019W\u0001\u0000\u0000\u0000\u001bY\u0001\u0000"+
		"\u0000\u0000\u001dc\u0001\u0000\u0000\u0000\u001fp\u0001\u0000\u0000\u0000"+
		"!r\u0001\u0000\u0000\u0000#z\u0001\u0000\u0000\u0000%&\u0005=\u0000\u0000"+
		"&\u0002\u0001\u0000\u0000\u0000\'(\u0005;\u0000\u0000(\u0004\u0001\u0000"+
		"\u0000\u0000)*\u0005+\u0000\u0000*\u0006\u0001\u0000\u0000\u0000+,\u0005"+
		".\u0000\u0000,\b\u0001\u0000\u0000\u0000-.\u0005(\u0000\u0000.\n\u0001"+
		"\u0000\u0000\u0000/0\u0005)\u0000\u00000\f\u0001\u0000\u0000\u000012\u0005"+
		"[\u0000\u00002\u000e\u0001\u0000\u0000\u000034\u0005,\u0000\u00004\u0010"+
		"\u0001\u0000\u0000\u000056\u0005]\u0000\u00006\u0012\u0001\u0000\u0000"+
		"\u000078\u0005{\u0000\u00008\u0014\u0001\u0000\u0000\u00009:\u0005}\u0000"+
		"\u0000:\u0016\u0001\u0000\u0000\u0000;<\u0005:\u0000\u0000<\u0018\u0001"+
		"\u0000\u0000\u0000=>\u0005s\u0000\u0000>?\u0005t\u0000\u0000?@\u0005r"+
		"\u0000\u0000@A\u0005i\u0000\u0000AB\u0005n\u0000\u0000BX\u0005g\u0000"+
		"\u0000CD\u0005n\u0000\u0000DE\u0005u\u0000\u0000EF\u0005m\u0000\u0000"+
		"FG\u0005b\u0000\u0000GH\u0005e\u0000\u0000HX\u0005r\u0000\u0000IJ\u0005"+
		"b\u0000\u0000JK\u0005o\u0000\u0000KL\u0005o\u0000\u0000LM\u0005l\u0000"+
		"\u0000MN\u0005e\u0000\u0000NO\u0005a\u0000\u0000OX\u0005n\u0000\u0000"+
		"PQ\u0005l\u0000\u0000QR\u0005i\u0000\u0000RS\u0005s\u0000\u0000SX\u0005"+
		"t\u0000\u0000TU\u0005m\u0000\u0000UV\u0005a\u0000\u0000VX\u0005p\u0000"+
		"\u0000W=\u0001\u0000\u0000\u0000WC\u0001\u0000\u0000\u0000WI\u0001\u0000"+
		"\u0000\u0000WP\u0001\u0000\u0000\u0000WT\u0001\u0000\u0000\u0000X\u001a"+
		"\u0001\u0000\u0000\u0000Y]\u0005\"\u0000\u0000Z\\\t\u0000\u0000\u0000"+
		"[Z\u0001\u0000\u0000\u0000\\_\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000"+
		"\u0000][\u0001\u0000\u0000\u0000^`\u0001\u0000\u0000\u0000_]\u0001\u0000"+
		"\u0000\u0000`a\u0005\"\u0000\u0000a\u001c\u0001\u0000\u0000\u0000bd\u0007"+
		"\u0000\u0000\u0000cb\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000"+
		"ec\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u001e\u0001\u0000"+
		"\u0000\u0000gh\u0005t\u0000\u0000hi\u0005r\u0000\u0000ij\u0005u\u0000"+
		"\u0000jq\u0005e\u0000\u0000kl\u0005f\u0000\u0000lm\u0005a\u0000\u0000"+
		"mn\u0005l\u0000\u0000no\u0005s\u0000\u0000oq\u0005e\u0000\u0000pg\u0001"+
		"\u0000\u0000\u0000pk\u0001\u0000\u0000\u0000q \u0001\u0000\u0000\u0000"+
		"rv\u0007\u0001\u0000\u0000su\u0007\u0002\u0000\u0000ts\u0001\u0000\u0000"+
		"\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000"+
		"\u0000\u0000w\"\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000y{\u0007"+
		"\u0003\u0000\u0000zy\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000"+
		"\u0000~\u007f\u0006\u0011\u0000\u0000\u007f$\u0001\u0000\u0000\u0000\u0007"+
		"\u0000W]epv|\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}