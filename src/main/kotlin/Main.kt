package cli

import interpreter.DMLInterpreter
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Użycie: java -jar dml-cli.jar <plik.dml>")
        return
    }

    val fileName = args[0]
    val file = File(fileName)

    if (!file.exists()) {
        println("Plik nie istnieje: $fileName")
        return
    }

    val code = file.readText()
    val interpreter = DMLInterpreter()
    interpreter.execute(code)
}
