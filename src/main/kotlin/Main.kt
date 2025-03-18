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
        println("File does not exist: $fileName")
        return
    }

    val code = file.readText()
    val interpreter = DMLInterpreter()
    interpreter.execute(code)
}
