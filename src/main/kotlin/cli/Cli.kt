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

    println("Error: Unknown command '$command'. Type 'dml help' for available commands.")
}

fun printHelp() {
    println("""
        DML Command Line Interface
        Usage: dml <command> [file]
        
        Commands:
          read <file>   - Read and execute a .dml file
          help          - Show available commands
    """.trimIndent())
}
