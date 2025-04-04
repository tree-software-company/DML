// Generated from /Users/szymonmastalerz/Documents/_prywatne-studia/tree/DML/src/main/DML.g4 by ANTLR 4.13.1

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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, TYPE=6, STRING=7, NUMBER=8, BOOLEAN=9, 
		IDENTIFIER=10, WS=11;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "TYPE", "STRING", "NUMBER", "BOOLEAN", 
			"IDENTIFIER", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "';'", "'+'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "TYPE", "STRING", "NUMBER", "BOOLEAN", 
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
		"\u0004\u0000\u000bd\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005"+
		"<\b\u0005\u0001\u0006\u0001\u0006\u0005\u0006@\b\u0006\n\u0006\f\u0006"+
		"C\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0004\u0007H\b\u0007\u000b"+
		"\u0007\f\u0007I\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0003\bU\b\b\u0001\t\u0001\t\u0005\tY\b\t\n\t\f\t\\"+
		"\t\t\u0001\n\u0004\n_\b\n\u000b\n\f\n`\u0001\n\u0001\n\u0001A\u0000\u000b"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0001\u0000\u0004\u0001\u0000"+
		"09\u0003\u0000AZ__az\u0004\u000009AZ__az\u0003\u0000\t\n\r\r  l\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0001\u0017\u0001\u0000\u0000\u0000\u0003\u0019"+
		"\u0001\u0000\u0000\u0000\u0005\u001b\u0001\u0000\u0000\u0000\u0007\u001d"+
		"\u0001\u0000\u0000\u0000\t\u001f\u0001\u0000\u0000\u0000\u000b;\u0001"+
		"\u0000\u0000\u0000\r=\u0001\u0000\u0000\u0000\u000fG\u0001\u0000\u0000"+
		"\u0000\u0011T\u0001\u0000\u0000\u0000\u0013V\u0001\u0000\u0000\u0000\u0015"+
		"^\u0001\u0000\u0000\u0000\u0017\u0018\u0005=\u0000\u0000\u0018\u0002\u0001"+
		"\u0000\u0000\u0000\u0019\u001a\u0005;\u0000\u0000\u001a\u0004\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0005+\u0000\u0000\u001c\u0006\u0001\u0000\u0000"+
		"\u0000\u001d\u001e\u0005(\u0000\u0000\u001e\b\u0001\u0000\u0000\u0000"+
		"\u001f \u0005)\u0000\u0000 \n\u0001\u0000\u0000\u0000!\"\u0005s\u0000"+
		"\u0000\"#\u0005t\u0000\u0000#$\u0005r\u0000\u0000$%\u0005i\u0000\u0000"+
		"%&\u0005n\u0000\u0000&<\u0005g\u0000\u0000\'(\u0005n\u0000\u0000()\u0005"+
		"u\u0000\u0000)*\u0005m\u0000\u0000*+\u0005b\u0000\u0000+,\u0005e\u0000"+
		"\u0000,<\u0005r\u0000\u0000-.\u0005b\u0000\u0000./\u0005o\u0000\u0000"+
		"/0\u0005o\u0000\u000001\u0005l\u0000\u000012\u0005e\u0000\u000023\u0005"+
		"a\u0000\u00003<\u0005n\u0000\u000045\u0005l\u0000\u000056\u0005i\u0000"+
		"\u000067\u0005s\u0000\u00007<\u0005t\u0000\u000089\u0005m\u0000\u0000"+
		"9:\u0005a\u0000\u0000:<\u0005p\u0000\u0000;!\u0001\u0000\u0000\u0000;"+
		"\'\u0001\u0000\u0000\u0000;-\u0001\u0000\u0000\u0000;4\u0001\u0000\u0000"+
		"\u0000;8\u0001\u0000\u0000\u0000<\f\u0001\u0000\u0000\u0000=A\u0005\""+
		"\u0000\u0000>@\t\u0000\u0000\u0000?>\u0001\u0000\u0000\u0000@C\u0001\u0000"+
		"\u0000\u0000AB\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000BD\u0001"+
		"\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000DE\u0005\"\u0000\u0000E\u000e"+
		"\u0001\u0000\u0000\u0000FH\u0007\u0000\u0000\u0000GF\u0001\u0000\u0000"+
		"\u0000HI\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000IJ\u0001\u0000"+
		"\u0000\u0000J\u0010\u0001\u0000\u0000\u0000KL\u0005t\u0000\u0000LM\u0005"+
		"r\u0000\u0000MN\u0005u\u0000\u0000NU\u0005e\u0000\u0000OP\u0005f\u0000"+
		"\u0000PQ\u0005a\u0000\u0000QR\u0005l\u0000\u0000RS\u0005s\u0000\u0000"+
		"SU\u0005e\u0000\u0000TK\u0001\u0000\u0000\u0000TO\u0001\u0000\u0000\u0000"+
		"U\u0012\u0001\u0000\u0000\u0000VZ\u0007\u0001\u0000\u0000WY\u0007\u0002"+
		"\u0000\u0000XW\u0001\u0000\u0000\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001"+
		"\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[\u0014\u0001\u0000\u0000"+
		"\u0000\\Z\u0001\u0000\u0000\u0000]_\u0007\u0003\u0000\u0000^]\u0001\u0000"+
		"\u0000\u0000_`\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000bc\u0006\n\u0000\u0000c\u0016"+
		"\u0001\u0000\u0000\u0000\u0007\u0000;AITZ`\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}