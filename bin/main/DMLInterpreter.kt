package interpreter

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLLexer
import parser.DMLParser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

class DMLInterpreter {

    fun execute(code: String) {
        val result = evaluate(code)
        println("Variables: $result")
    }

    fun evaluate(code: String): Map<String, Any?> {
        val lexer = DMLLexer(CharStreams.fromString(code))
        val tokens = CommonTokenStream(lexer)
        val parser = DMLParser(tokens)
        val tree = parser.file()

        val executor = DMLExecutor()
        executor.execute(tree)
        return executor.getSymbolTable()
    }

    fun toJson(map: Map<String, Any?>): String {
        val jsonElement = convertToJsonElement(map)
        return Json { prettyPrint = true }.encodeToString(JsonElement.serializer(), jsonElement)
    }

    private fun convertToJsonElement(value: Any?): JsonElement {
        return when (value) {
            null -> JsonNull
            is String -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is Map<*, *> -> {
                val content = value.entries.associate {
                    (it.key as? String ?: error("Only string keys allowed in maps")) to convertToJsonElement(it.value)
                }
                JsonObject(content)
            }
            is List<*> -> JsonArray(value.map { convertToJsonElement(it) })
            else -> JsonPrimitive(value.toString())
        }
    }
}
