package cli

import interpreter.DMLExecutor
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
            println("DML version 1.0.0")
            return
        }

        val (format, filePath) = if (args[0] == "-r" && args.size >= 3) {
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
            val executor = DMLExecutor()
            executor.executeFile(file)

            when (format.lowercase()) {
                "json" -> printJson(executor.getAllRaw())
                "yaml" -> printYaml(executor.getAllRaw())
                "xml" -> printXml(executor.getAllRaw())
                "text", "dml" -> executor.getSymbolTable().forEach { (key, value) ->
                    println("$key = $value")
                }
                else -> {
                    println("Error: Unknown format '$format'")
                    println("Supported formats: json, yaml, xml, text, dml")
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
            
            Usage:
              dml <file.dml>                    Run a DML file
              dml -r <format> <file.dml>        Run and output in specified format
              dml -h, --help                    Show this help message
              dml -v, --version                 Show version information
            
            Output Formats:
              text, dml       Plain text output (default)
              json            JSON format
              yaml            YAML format
              xml             XML format
            
            Examples:
              dml test.dml                      Run test.dml
              dml -r json test.dml              Run and output as JSON
              dml -r yaml test.dml              Run and output as YAML
            
            Options:
              -d, --debug                       Show debug information on error
        """.trimIndent())
    }

    private fun printJson(variables: Map<String, Any?>) {
        println("{")
        variables.entries.forEachIndexed { index, (key, value) ->
            val jsonValue = when (value) {
                is String -> "\"$value\""
                is Number -> value.toString()
                is Boolean -> value.toString()
                is List<*> -> "[${value.joinToString(", ") { formatJsonValue(it) }}]"
                is Map<*, *> -> formatJsonMap(value)
                null -> "null"
                else -> "\"$value\""
            }
            val comma = if (index < variables.size - 1) "," else ""
            println("  \"$key\": $jsonValue$comma")
        }
        println("}")
    }

    private fun formatJsonValue(value: Any?): String {
        return when (value) {
            is String -> "\"$value\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            is List<*> -> "[${value.joinToString(", ") { formatJsonValue(it) }}]"
            is Map<*, *> -> formatJsonMap(value)
            null -> "null"
            else -> "\"$value\""
        }
    }

    private fun formatJsonMap(map: Map<*, *>): String {
        val entries = map.entries.joinToString(", ") { (k, v) ->
            "\"$k\": ${formatJsonValue(v)}"
        }
        return "{$entries}"
    }

    private fun printYaml(variables: Map<String, Any?>) {
        variables.forEach { (key, value) ->
            when (value) {
                is String -> println("$key: \"$value\"")
                is Number -> println("$key: $value")
                is Boolean -> println("$key: $value")
                is List<*> -> {
                    println("$key:")
                    value.forEach { item ->
                        println("  - ${formatYamlValue(item)}")
                    }
                }
                is Map<*, *> -> {
                    println("$key:")
                    value.forEach { (k, v) ->
                        println("  $k: ${formatYamlValue(v)}")
                    }
                }
                null -> println("$key: null")
                else -> println("$key: $value")
            }
        }
    }

    private fun formatYamlValue(value: Any?): String {
        return when (value) {
            is String -> "\"$value\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            null -> "null"
            else -> value.toString()
        }
    }

    private fun printXml(variables: Map<String, Any?>) {
        println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        println("<variables>")
        variables.forEach { (key, value) ->
            when (value) {
                is String -> println("  <$key>$value</$key>")
                is Number -> println("  <$key>$value</$key>")
                is Boolean -> println("  <$key>$value</$key>")
                is List<*> -> {
                    println("  <$key>")
                    value.forEach { item ->
                        println("    <item>$item</item>")
                    }
                    println("  </$key>")
                }
                is Map<*, *> -> {
                    println("  <$key>")
                    value.forEach { (k, v) ->
                        println("    <$k>$v</$k>")
                    }
                    println("  </$key>")
                }
                null -> println("  <$key/>")
                else -> println("  <$key>$value</$key>")
            }
        }
        println("</variables>")
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
