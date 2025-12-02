package cli

import interpreter.DMLInterpreter
import java.io.File

class Cli {
    fun run(args: Array<String>) {
        when {
            args.isEmpty() -> {
                println("DML Interpreter")
                println("Usage: dml <file.dml> or dml -i for interactive mode")
                println("       dml -r <format> <file.dml> - convert to specified format")
                return
            }
            args[0] == "-i" || args[0] == "--interactive" -> {
                runInteractiveMode()
            }
            args[0] == "-r" && args.size >= 3 -> {
                // Format: dml -r <format> <file.dml>
                val format = args[1]
                val filePath = args[2]

                if (!filePath.endsWith(".dml")) {
                    println("Error: File must have .dml extension")
                    return
                }

                runConversionMode(format, filePath)
            }
            args.size == 1 -> {
                // Format: dml <file.dml>
                val filePath = args[0]
                if (!filePath.endsWith(".dml")) {
                    println("Error: File must have .dml extension")
                    return
                }

                try {
                    val interpreter = DMLInterpreter()
                    interpreter.executeFile(filePath)
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
            else -> {
                println("Error: Invalid arguments")
                println("Usage: dml <file.dml> or dml -i for interactive mode")
                println("       dml -r <format> <file.dml> - convert to specified format")
            }
        }
    }

    private fun runConversionMode(format: String, filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                println("Error: File not found: $filePath")
                return
            }

            val content = file.readText()
            val interpreter = DMLInterpreter()
            val data = interpreter.evaluate(content)

            val result = when (format.lowercase()) {
                "json" -> interpreter.toJson(data)
                "yaml" -> interpreter.toYaml(data)
                "xml" -> interpreter.toXml(data)
                "properties" -> interpreter.toProperties(data)
                "plist" -> interpreter.toPlist(data)
                "dml" -> interpreter.format(content)
                else -> {
                    println("Error: Unsupported format '$format'")
                    println("Supported formats: json, yaml, xml, properties, plist, dml")
                    return
                }
            }

            println(result)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun runInteractiveMode() {
        println("DML Interactive Mode")
        println("Type 'exit' to quit")

        val interpreter = DMLInterpreter()

        while (true) {
            print("dml> ")
            val input = readlnOrNull() ?: break

            if (input.trim().lowercase() == "exit") {
                break
            }

            if (input.trim().isEmpty()) {
                continue
            }

            try {
                interpreter.executeString(input)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}

fun main(args: Array<String>) {
    Cli().run(args)
}

fun printHelp() {
    print("""
        DML Command Line Interface (Short mode)
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
