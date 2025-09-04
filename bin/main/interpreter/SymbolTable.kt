package interpreter

class SymbolTable {
    private val variables = mutableMapOf<String, Any?>()
    private val privateKeys = mutableSetOf<String>()

    fun setVariable(name: String, value: Any?, isPrivate: Boolean = false) {
        variables[name] = value
        if (isPrivate) privateKeys.add(name)
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }

    fun getAll(): Map<String, Any?> {
        return variables.filterKeys { it !in privateKeys }
    }

    fun getAllRaw(): Map<String, Any?> {
        return variables
    }

    fun isPrivate(name: String): Boolean = privateKeys.contains(name)

    fun printVariables() {
        println("Variables: ${getAll()}")
    }
}
