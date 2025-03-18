package interpreter

class SymbolTable {
    private val variables = mutableMapOf<String, Any?>()

    fun setVariable(name: String, value: Any?) {
        variables[name] = value
    }

    fun getVariable(name: String): Any? {
        return variables[name] ?: error("Nieznana zmienna: $name")
    }

    fun printVariables() {
        println("Zmienne: $variables")
    }
}
