package cli

import interpreter.DMLExecutor
import interpreter.DMLInterpreter
import java.io.File

class Cli {
    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        if (args[0] in listOf("-h", "--help", "help")) {
            printHelp()
            return
        }

        if (args[0] in listOf("-v", "--version", "version")) {
            println("DML version 0.8.1")
            return
        }

        if (args[0] in listOf("-u", "--update", "update")) {
            println("Updating DML via Homebrew...")
            val commands = listOf(
                "brew uninstall dml",
                "brew untap tree-software-company/dml",
                "brew tap tree-software-company/dml",
                "brew trust tree-software-company/dml",
                "brew install dml"
            )
            for (cmd in commands) {
                println("-> $cmd")
                val process = ProcessBuilder(*cmd.split(" ").toTypedArray())
                    .inheritIO()
                    .start()
                val exitCode = process.waitFor()
                if (exitCode != 0) {
                    println("Failed to update DML: '$cmd' exited with code $exitCode")
                    return
                }
            }
            println("DML updated successfully.")
            return
        }
        val isWrite = args[0] == "-w"
        val (format, filePath) = if ((args[0] == "-r" || args[0] == "-w") && args.size >= 3) {
            args[1] to args[2]
        } else if (args[0].startsWith("-")) {
            println("Error: Unknown option: ${args[0]}")
            println("Use -h or --help for usage information")
            return
        } else {
            "text" to args[0]
        }

        val file = File(filePath)
        if (!file.exists()) {
            println("Error: File not found: $filePath")
            return
        }

        try {
            if (isWrite) {
                val executor = DMLExecutor()
                executor.executeFile(file)
                when (format.lowercase()) {
                    "xml" -> printXml(executor.getSymbolTable())
                    "json" -> printJson(executor.getSymbolTable())
                    "yaml" -> printYaml(executor.getSymbolTable())
                    else -> executor.getSymbolTable().forEach { (key, value) -> println("$key = $value") }
                }
            } else {
                when (format.lowercase()) {
                    "xml" -> {
                        val interpreter = DMLInterpreter()
                        val map = interpreter.convertXmlToMap(DMLExecutor.readFileChecked(file))
                        map.forEach { (key, value) -> println("$key = $value") }
                    }
                    "plist" -> {
                        val interpreter = DMLInterpreter()
                        val map = interpreter.convertPlistToMap(DMLExecutor.readFileChecked(file))
                        map.forEach { (key, value) -> println("$key = $value") }
                    }
                    "json" -> {
                        val interpreter = DMLInterpreter()
                        val jsonObj = kotlinx.serialization.json.Json.parseToJsonElement(DMLExecutor.readFileChecked(file))
                            .let { it as? kotlinx.serialization.json.JsonObject }
                            ?: throw IllegalArgumentException("Top-level JSON must be an object")
                        val map = interpreter.convertJsonToMap(jsonObj)
                        map.forEach { (key, value) -> println("$key = $value") }
                    }
                    "yaml" -> {
                        val interpreter = DMLInterpreter()
                        val map = interpreter.convertYamlToMap(DMLExecutor.readFileChecked(file))
                        map.forEach { (key, value) -> println("$key = $value") }
                    }
                    "text", "dml" -> {
                        val executor = DMLExecutor()
                        executor.executeFile(file)
                        executor.getSymbolTable().forEach { (key, value) ->
                            println("$key = $value")
                        }
                    }
                    else -> {
                        println("Error: Unknown format '$format'")
                        println("Supported formats: json, yaml, xml, plist, yaml, text, dml")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            if (args.contains("--debug") || args.contains("-d")) {
                e.printStackTrace()
            }
        }
    }

    private fun printHelp() {
        println("""
            DML - Data Markup Language
            
            Commands:
          -r dml <file>            - Read and execute a .dml file
          -r json <file>           - Convert .json to .dml
          -r yaml <file>           - Convert .yaml to .dml
          -r xml <file>            - Convert .xml to .dml
          -r plist <file>          - Convert .plist to .dml
          -r properties <file>     - Convert .properties to .dml
          -w json <file>           - Convert .dml to .json
          -w yaml <file>           - Convert .dml to .yaml
          -w xml <file>            - Convert .dml to .xml
          -w plist <file>          - Convert .dml to .plist
          -w properties <file>     - Convert .dml to .properties
          -l <file>                - Lint .dml file
          -f <file>                - Format .dml file
          -e '<expression>' <file> - Add DML expression to file
          -n <file>                - Create a new DML file with a template
          -h, --help               - Show this help
          -v, --version            - Show lang version
          -u, update               - Update DML via Homebrew
        """.trimIndent())
    }

    private fun printJson(variables: Map<String, Any?>) {
        println("{")
        variables.entries.forEachIndexed { index, (key, value) ->
            val jsonValue = when (value) {
                is String -> "\"${escapeJsonString(value)}\""
                is Number -> value.toString()
                is Boolean -> value.toString()
                is List<*> -> "[${value.joinToString(", ") { formatJsonValue(it) }}]"
                is Map<*, *> -> formatJsonMap(value)
                null -> "null"
                else -> "\"${escapeJsonString(value.toString())}\""
            }
            val comma = if (index < variables.size - 1) "," else ""
            println("  \"${escapeJsonString(key)}\": $jsonValue$comma")
        }
        println("}")
    }

    private fun formatJsonValue(value: Any?): String {
        return when (value) {
            is String -> "\"${escapeJsonString(value)}\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            is List<*> -> "[${value.joinToString(", ") { formatJsonValue(it) }}]"
            is Map<*, *> -> formatJsonMap(value)
            null -> "null"
            else -> "\"${escapeJsonString(value.toString())}\""
        }
    }

    private fun formatJsonMap(map: Map<*, *>): String {
        val entries = map.entries.joinToString(", ") { (k, v) ->
            "\"${escapeJsonString(k.toString())}\": ${formatJsonValue(v)}"
        }
        return "{$entries}"
    }

    private fun escapeJsonString(value: String): String {
        val sb = StringBuilder()
        for (ch in value) {
            when (ch) {
                '\\' -> sb.append("\\\\")
                '"'  -> sb.append("\\\"")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                else -> if (ch.code < 0x20) {
                    sb.append("\\u%04x".format(ch.code))
                } else {
                    sb.append(ch)
                }
            }
        }
        return sb.toString()
    }

    private fun printYaml(variables: Map<String, Any?>) {
        variables.forEach { (key, value) ->
            val safeKey = escapeYamlKey(key)
            when (value) {
                is String  -> println("$safeKey: \"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\"")
                is Number  -> println("$safeKey: $value")
                is Boolean -> println("$safeKey: $value")
                is List<*> -> {
                    println("$safeKey:")
                    value.forEach { item ->
                        println("  - ${formatYamlValue(item)}")
                    }
                }
                is Map<*, *> -> {
                    println("$safeKey:")
                    value.forEach { (k, v) ->
                        println("  ${escapeYamlKey(k.toString())}: ${formatYamlValue(v)}")
                    }
                }
                null -> println("$safeKey: null")
                else -> println("$safeKey: $value")
            }
        }
    }

    private fun formatYamlValue(value: Any?): String {
        return when (value) {
            is String  -> "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""
            is Number  -> value.toString()
            is Boolean -> value.toString()
            null       -> "null"
            else       -> value.toString()
        }
    }

    private fun escapeYamlKey(key: String): String {
        val needsQuoting = key.isEmpty() ||
            key.any { it in ":,[]{}#&*?|<>=!%@`\"'\\\n\r" || it.isWhitespace() }
        return if (needsQuoting) {
            "\"${key.replace("\\", "\\\\").replace("\"", "\\\"")}\""
        } else {
            key
        }
    }

    private fun printXml(variables: Map<String, Any?>) {
        println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        println("<variables>")
        variables.forEach { (key, value) ->
            val safeKey = sanitizeXmlTagName(key)
            when (value) {
                is String -> println("  <$safeKey>${escapeXml(value)}</$safeKey>")
                is Number -> println("  <$safeKey>$value</$safeKey>")
                is Boolean -> println("  <$safeKey>$value</$safeKey>")
                is List<*> -> {
                    println("  <$safeKey>")
                    value.forEach { item ->
                        println("    <item>${escapeXml(item.toString())}</item>")
                    }
                    println("  </$safeKey>")
                }
                is Map<*, *> -> {
                    println("  <$safeKey>")
                    value.forEach { (k, v) ->
                        val safeK = sanitizeXmlTagName(k.toString())
                        println("    <$safeK>${escapeXml(v.toString())}</$safeK>")
                    }
                    println("  </$safeKey>")
                }
                null -> println("  <$safeKey/>")
                else -> println("  <$safeKey>${escapeXml(value.toString())}</$safeKey>")
            }
        }
        println("</variables>")
    }

    private fun escapeXml(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private fun sanitizeXmlTagName(name: String): String {
        val sanitized = name.replace(Regex("[^a-zA-Z0-9_\\-.]"), "_")
        return if (sanitized.isEmpty() || sanitized[0].isDigit() || sanitized[0] == '-' || sanitized[0] == '.') {
            "_$sanitized"
        } else {
            sanitized
        }
    }
}

fun printHelp() {
    println("""
        DML Command Line Interface
        Usage: dml <command> [args]

        Commands:
          -r dml <file>            - Read and execute a .dml file
          -r json <file>           - Convert .json to .dml
          -r yaml <file>           - Convert .yaml to .dml
          -r xml <file>            - Convert .xml to .dml
          -r plist <file>          - Convert .plist to .dml
          -r properties <file>     - Convert .properties to .dml
          -w json <file>           - Convert .dml to .json
          -w yaml <file>           - Convert .dml to .yaml
          -w xml <file>            - Convert .dml to .xml
          -w plist <file>          - Convert .dml to .plist
          -w properties <file>     - Convert .dml to .properties
          -l <file>                - Lint .dml file
          -f <file>                - Format .dml file
          -e '<expression>' <file> - Add DML expression to file
          -n <file>                - Create a new DML file with a template
          -h, --help               - Show this help
          -v, --version            - Show lang version
          -u, update               - Update DML via Homebrew
    """.trimIndent())
}

fun main(args: Array<String>) {
    Cli().run(args)
}
