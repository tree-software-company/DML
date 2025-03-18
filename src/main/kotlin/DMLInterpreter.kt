package interpreter

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLLexer
import parser.DMLParser

class DMLInterpreter {
    fun execute(code: String) {
        // 1. Tworzymy lexer i parser
        val lexer = DMLLexer(CharStreams.fromString(code))
        val tokens = CommonTokenStream(lexer)
        val parser = DMLParser(tokens)

        // 2. Parsujemy kod
        val tree = parser.file()

        // 3. Wykonujemy kod
        val executor = DMLExecutor()
        executor.execute(tree)
    }
}
