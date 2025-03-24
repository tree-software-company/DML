package interpreter

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLLexer
import parser.DMLParser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions


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

    fun toYaml(map: Map<String, Any?>): String {
        val options = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
            indent = 2
        }
    
        val yaml = Yaml(options)
        return yaml.dump(map)
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

    fun format(code: String): String {
        val data = evaluate(code)
        return buildDmlString(data)
    }    

    fun validate(code: String) {
        evaluate(code)
    }

    private fun buildDmlString(map: Map<String, Any?>, indent: String = ""): String {
        return map.entries.joinToString("\n") { (key, value) ->
            when (value) {
                is String -> "$indent" + "string $key = \"${value.replace("\"", "\\\"")}\";"
                is Int, is Double -> "$indent" + "number $key = $value;"
                is Boolean -> "$indent" + "boolean $key = $value;"
                is List<*> -> {
                    val listValues = value.joinToString(", ") { formatLiteral(it) }
                    "$indent" + "list $key = [$listValues];"
                }
                is Map<*, *> -> {
                    val nestedIndent = indent + "  "
                    val entries = value.entries.joinToString(",\n") {
                        val k = it.key as? String ?: error("Only string keys allowed in map")
                        val v = formatLiteral(it.value, nestedIndent)
                        "$nestedIndent\"$k\": $v"
                    }
                    "$indent" + "map $key = {\n$entries\n$indent};"
                }
                else -> "$indent" + "string $key = \"${value.toString()}\";"
            }
        }
    }    
    
    private fun formatLiteral(value: Any?, indent: String = ""): String {
        return when (value) {
            null -> "null"
            is String -> "\"${value.replace("\"", "\\\"")}\""
            is Number, is Boolean -> value.toString()
            is Map<*, *> -> {
                val nestedIndent = indent + "  "
                val formatted = value.entries.joinToString(",\n") {
                    val k = it.key as? String ?: error("Only string keys allowed in map")
                    val v = formatLiteral(it.value, nestedIndent)
                    "$nestedIndent\"$k\": $v"
                }
                "{\n$formatted\n$indent}"
            }
            is List<*> -> {
                val formatted = value.joinToString(", ") { formatLiteral(it, indent) }
                "[$formatted]"
            }
            else -> "\"${value.toString()}\""
        }
    }       
}
