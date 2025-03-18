package cli

import interpreter.DMLInterpreter
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: java -jar dml-cli.jar <file.dml>")
        return
    }

    val fileName = args[0]
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
}
