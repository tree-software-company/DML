package cli

import interpreter.DMLInterpreter
import java.io.File

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        printHelp()
        return
    }

    val command = args[0]

    if (command == "help") {
        printHelp()
        return
    }

    if (command == "read") {
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
            interpreter.execute(code)
        } catch (e: Exception) {
            println("Runtime Error: ${e.message}")
        }
        return
    }

    if (command == "write" && args.size >= 3 && args[1] == "json") {
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
    
            val jsonOutput = interpreter.toJson(result)
            val outputFileName = fileName.replaceAfterLast('.', "json")
            File(outputFileName).writeText(jsonOutput)
    
            println("✅ File saved as $outputFileName")
        } catch (e: Exception) {
            println("Runtime Error: ${e.message}")
        }
        return
    }  
    
    if (command == "write" && args.size >= 3 && args[1] == "yaml") {
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
    
            val yamlOutput = interpreter.toYaml(result)
            val outputFileName = fileName.replaceAfterLast('.', "yaml")
            File(outputFileName).writeText(yamlOutput)
    
            println("✅ File saved as $outputFileName")
        } catch (e: Exception) {
            println("Runtime Error: ${e.message}")
        }
        return
    }
    
    if (command == "lint") {
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
    
    if (command == "format") {
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

    println("Error: Unknown command '$command'. Type 'dml help' for available commands.")
}

fun printHelp() {
    println("""
        DML Command Line Interface
        Usage: dml <command> [file]
        
        Commands:
          read <file>       - Read and execute a .dml file
          write json <file> - Convert .dml file for .json file
          write yaml <file> - Convert .dml file for .yaml file
          lint <file>       - Check if .dml file will validate
          format <file>     - Make .dml code more beauty
          help              - Show available commands
    """.trimIndent())
}
