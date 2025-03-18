package interpreter

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLLexer
import parser.DMLParser

class DMLInterpreter {
    fun execute(code: String) {
        val lexer = DMLLexer(CharStreams.fromString(code))
        val tokens = CommonTokenStream(lexer)
        val parser = DMLParser(tokens)

        val tree = parser.file()

        val executor = DMLExecutor()
        executor.execute(tree)
    }
}
