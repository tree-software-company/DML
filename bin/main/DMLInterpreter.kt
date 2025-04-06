package interpreter

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parser.DMLLexer
import parser.DMLParser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.*
import kotlinx.serialization.decodeFromString
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions
import java.lang.StringBuilder

class DMLInterpreter {

    fun execute(code: String): Map<String, Any?> {
        return evaluate(code)
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
        val openIndex = code.indexOf("/*")
        val closeIndex = code.indexOf("*/")
    
        if (openIndex != -1 && (closeIndex == -1 || closeIndex < openIndex)) {
            throw IllegalArgumentException("Unclosed block comment (/* ... */)")
        }
        
        evaluate(code)
    }    

    fun buildDmlString(map: Map<String, Any?>, indent: String = ""): String {
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
    
    fun toXml(map: Map<String, Any?>): String {
        val sb = StringBuilder()
        sb.appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        sb.appendLine("<dml>")
        map.forEach { (key, value) ->
            sb.append(formatXmlValue(key, value, indent = "  "))
        }
        sb.appendLine("</dml>")
        return sb.toString()
    }
    
    fun toProperties(map: Map<String, Any?>): String {
        val flatMap = flattenMap(map)
        return flatMap.entries.joinToString("\n") { (key, value) ->
            "$key=${value?.toString()?.replace("\n", "\\n")}"
        }
    }

    private fun flattenMap(map: Map<String, Any?>, prefix: String = ""): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
    
        for ((key, value) in map) {
            val fullKey = if (prefix.isEmpty()) key else "$prefix.$key"
    
            when (value) {
                is Map<*, *> -> {
                    val nested = flattenMap(value as Map<String, Any?>, fullKey)
                    result.putAll(nested)
                }
                is List<*> -> {
                    result[fullKey] = value.joinToString(",") { it.toString() }
                }
                else -> {
                    result[fullKey] = value
                }
            }
        }
    
        return result
    }
    
    private fun formatXmlValue(key: String, value: Any?, indent: String): String {
        return when (value) {
            null -> "$indent<$key />\n"
            is String, is Number, is Boolean -> "$indent<$key>${value.toString()}</$key>\n"
            is List<*> -> {
                val inner = value.joinToString("") { formatXmlValue("item", it, indent + "  ") }
                "$indent<$key>\n$inner$indent</$key>\n"
            }
            is Map<*, *> -> {
                val inner = value.entries.joinToString("") { (k, v) ->
                    formatXmlValue(k.toString(), v, indent + "  ")
                }
                "$indent<$key>\n$inner$indent</$key>\n"
            }
            else -> "$indent<$key>${value.toString()}</$key>\n"
        }
    }
    
    fun toPlist(map: Map<String, Any?>): String {
        val sb = StringBuilder()
        sb.appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        sb.appendLine("""<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">""")
        sb.appendLine("<plist version=\"1.0\">")
        sb.appendLine("<dict>")
        map.forEach { (key, value) ->
            sb.append(formatPlistValue(key, value, indent = "  "))
        }
        sb.appendLine("</dict>")
        sb.appendLine("</plist>")
        return sb.toString()
    }
    
    private fun formatPlistValue(key: String, value: Any?, indent: String): String {
        val escapedKey = key.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        val keyTag = "$indent<key>$escapedKey</key>\n"
    
        return when (value) {
            null -> keyTag + "$indent<string>null</string>\n"
            is String -> keyTag + "$indent<string>${escapeXml(value)}</string>\n"
            is Boolean -> keyTag + "$indent<${if (value) "true" else "false"}/>\n"
            is Int, is Long, is Double, is Float -> keyTag + "$indent<integer>$value</integer>\n"
            is List<*> -> {
                val arrayItems = value.joinToString("") {
                    "$indent  <string>${escapeXml(it.toString())}</string>\n"
                }
                keyTag + "$indent<array>\n$arrayItems$indent</array>\n"
            }
            is Map<*, *> -> {
                val nested = value.entries.joinToString("") {
                    val nestedKey = it.key.toString()
                    val nestedValue = formatPlistValue(nestedKey, it.value, indent + "  ")
                    nestedValue
                }
                keyTag + "$indent<dict>\n$nested$indent</dict>\n"
            }
            else -> keyTag + "$indent<string>${escapeXml(value.toString())}</string>\n"
        }
    }
    
    private fun escapeXml(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }   

    fun convertJsonToMap(obj: JsonObject): Map<String, Any?> {
        return obj.mapValues { (_, value) -> convertJsonValue(value) }
    }
    
    private fun convertJsonValue(value: JsonElement): Any? {
        return when (value) {
            is JsonNull -> null
            is JsonPrimitive -> {
                when {
                    value.intOrNull != null -> value.int
                    value.longOrNull != null -> value.long
                    value.doubleOrNull != null -> value.double
                    value.booleanOrNull != null -> value.boolean
                    value.isString -> value.content
                    else -> value.content
                }
            }
            is JsonArray -> value.map { convertJsonValue(it) }
            is JsonObject -> convertJsonToMap(value)
            else -> null
        }
    }

    fun convertYamlToMap(yamlText: String): Map<String, Any?> {
        val yaml = Yaml()
        val loaded = yaml.load<Any>(yamlText)
        if (loaded !is Map<*, *>) {
            throw IllegalArgumentException("Top-level YAML must be a map")
        }
    
        @Suppress("UNCHECKED_CAST")
        return loaded as Map<String, Any?>
    }    
     
}
