package cli

import interpreter.DMLInterpreter
import java.io.File

class Cli {
    fun run(args: Array<String>) {
        when {
            args.isEmpty() -> {
                printHelp()
                return
            }
            args[0] == "-h" || args[0] == "--help" -> {
                printHelp()
                return
            }
            args[0] == "-v" || args[0] == "--version" -> {
                println("DML version 0.6.2")
                return
            }
            args[0] == "-u" || args[0] == "--update" -> {
                updateDML()
                return
            }
            args[0] == "-i" || args[0] == "--interactive" -> {
                runInteractiveMode()
            }
            args[0] == "-r" && args.size >= 3 -> {
                val format = args[1]
                val filePath = args[2]
                runConversionMode(format, filePath)
            }
            args.size == 1 -> {
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
                printHelp()
            }
        }
    }

    private fun updateDML() {
        try {
            println("🔄 Updating DML via Homebrew...")
            val commands = listOf(
                "brew uninstall dml",
                "brew untap tree-software-company/dml",
                "brew tap tree-software-company/dml",
                "brew install dml"
            )
            for (cmd in commands) {
                println("→ $cmd")
                val process = ProcessBuilder(*cmd.split(" ").toTypedArray())
                    .inheritIO()
                    .start()
                process.waitFor()
            }
            println("✅ DML updated successfully.")
        } catch (e: Exception) {
            println("❌ Failed to update DML: ${e.message}")
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
                else -> {
                    println("Error: Unknown format '$format'")
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
            val input = readLine() ?: break
            
            if (input.trim() == "exit") break
            
            try {
                interpreter.executeString(input)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
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
