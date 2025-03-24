package interpreter

class SymbolTable {
    private val variables = mutableMapOf<String, Any?>()

    fun setVariable(name: String, value: Any?) {
        variables[name] = value
    }

    fun getVariable(name: String): Any? {
        return variables[name] ?: error("Unknown variable: $name")
    }

    fun printVariables() {
        println("Variables: $variables")
    }

    fun getAll(): Map<String, Any?> = variables
}
