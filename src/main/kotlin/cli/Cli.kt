package cli

import interpreter.DMLInterpreter
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.Json
import java.io.File

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        printHelp()
        return
    }

    val command = args[0] 

    when (command) {
        "--help", "-h" -> {
            printHelp()
            return
        }

        "-r" -> {
            if (args.size < 3) {
                println("Usage: dml -r <dml|json|yaml> <file>")
                return
            }

            val mode = args[1]
            val fileName = args[2]
            val file = File(fileName)

            if (!file.exists()) {
                println("Error: File '$fileName' does not exist.")
                return
            }


            val interpreter = DMLInterpreter()

            when (mode) {
                "dml" -> {
                    val code = file.readText()
                    try {
                        interpreter.execute(code)
                    } catch (e: Exception) {
                        println("Runtime Error: ${e.message}")
                    }
                }

                "json" -> {
                    try {
                        val jsonText = file.readText()
                        val jsonElement = Json.parseToJsonElement(jsonText)
                        if (jsonElement !is JsonObject) {
                            throw IllegalArgumentException("Top-level JSON must be an object")
                        }
                        val jsonMap = interpreter.convertJsonToMap(jsonElement)
                        val dmlCode = interpreter.buildDmlString(jsonMap)
                        val outputFileName = fileName.replaceAfterLast('.', "dml")
                        File(outputFileName).writeText(dmlCode)
                        println("✅ JSON converted to $outputFileName")
                    } catch (e: Exception) {
                        println("❌ Failed to convert JSON to DML: ${e.message}")
                    }
                }

                "yaml" -> {
                    try {
                        val yamlText = file.readText()
                        val yamlMap = interpreter.convertYamlToMap(yamlText)
                        val dmlCode = interpreter.buildDmlString(yamlMap)
                        val outputFileName = fileName.replaceAfterLast('.', "dml")
                        File(outputFileName).writeText(dmlCode)
                        println("✅ YAML converted to $outputFileName")
                    } catch (e: Exception) {
                        println("❌ Failed to convert YAML to DML: ${e.message}")
                    }
                }

                "xml" -> {
                    try {
                        val xmlText = file.readText()
                        val interpreter = DMLInterpreter()
                        val xmlMap = interpreter.convertXmlToMap(xmlText)
                        val dmlCode = interpreter.buildDmlString(xmlMap)
                        val outputFileName = fileName.replaceAfterLast('.', "dml")
                        File(outputFileName).writeText(dmlCode)
                        println("✅ XML converted to $outputFileName")
                    } catch (e: Exception) {
                        println("❌ Failed to convert XML to DML: ${e.message}")
                    }
                }


                else -> println("Unknown read mode '$mode'")
            }
            return
        }

        "-w" -> {
            if (args.size < 3) {
                println("Usage: dml -w <json|yaml|xml|plist|properties> <file>")
                return
            }

            val format = args[1]
            val fileName = args[2]
            val file = File(fileName)

            if (!file.exists()) {
                println("Error: File '$fileName' does not exist.")
                return
            }

            val code = file.readText()
            try {
                val interpreter = DMLInterpreter()
                val result = interpreter.evaluate(code)

                val (output, ext) = when (format) {
                    "json" -> interpreter.toJson(result) to "json"
                    "yaml" -> interpreter.toYaml(result) to "yaml"
                    "xml" -> interpreter.toXml(result) to "xml"
                    "properties" -> interpreter.toProperties(result) to "properties"
                    "plist" -> interpreter.toPlist(result) to "plist"
                    else -> {
                        println("Unknown write format '$format'")
                        return
                    }
                }

                val outputFileName = fileName.replaceAfterLast('.', ext)
                File(outputFileName).writeText(output)
                println("✅ File saved as $outputFileName")
            } catch (e: Exception) {
                println("Runtime Error: ${e.message}")
            }

            return
        }

        "-l" -> {
            if (args.size < 2) {
                println("Error: No file specified.")
                return
            }

            val fileName = args[1]
            val file = File(fileName)

            if (!file.exists()) {
                println("Error: File '$fileName' does not exist.")
                return
            }

            val code = file.readText()
            try {
                val interpreter = DMLInterpreter()
                interpreter.validate(code)
                println("✅ Lint successful: No errors found.")
            } catch (e: Exception) {
                println("❌ Lint failed: ${e.message}")
            }

            return
        }

        "-f" -> {
            if (args.size < 2) {
                println("Error: No file specified.")
                return
            }

            val fileName = args[1]
            val file = File(fileName)

            if (!file.exists()) {
                println("Error: File '$fileName' does not exist.")
                return
            }

            val code = file.readText()
            try {
                val interpreter = DMLInterpreter()
                val formatted = interpreter.format(code)
                file.writeText(formatted)
                println("✅ File formatted successfully: $fileName")
            } catch (e: Exception) {
                println("❌ Format failed: ${e.message}")
            }

            return
        }

        "-e" -> {
            if (args.size != 3) {
                println("Usage: dml -e '<expression>' <file>")
                return
            }

            val expression = args[1]
            val fileName = args[2]
            val file = File(fileName)

            try {
                if (!file.exists()) file.createNewFile()
                val addNewLine = file.readText().isNotBlank()
                val toWrite = (if (addNewLine) "\n" else "") + expression.trim()
                file.appendText(toWrite)
                println("✅ Expression written to ${file.name}")
            } catch (e: Exception) {
                println("❌ Failed to write expression: ${e.message}")
            }

            return
        }

        else -> println("Error: Unknown command '$command'. Type 'dml -h' for help.")
    }
}

fun printHelp() {
    println("""
        DML Command Line Interface (Short mode)
        Usage: dml <command> [args]

        Commands:
          -r dml <file>            - Read and execute a .dml file
          -r json <file>           - Convert .json to .dml
          -r yaml <file>           - Convert .yaml to .dml
          -w json <file>           - Convert .dml to .json
          -w yaml <file>           - Convert .dml to .yaml
          -w xml <file>            - Convert .dml to .xml
          -w plist <file>          - Convert .dml to .plist
          -w properties <file>     - Convert .dml to .properties
          -l <file>                - Lint .dml file
          -f <file>                - Format .dml file
          -e '<expression>' <file> - Add DML expression to file
          -h, --help               - Show this help
    """.trimIndent())
}
