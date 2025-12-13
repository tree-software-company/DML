package interpreter

class SymbolTable {
    private val variables = mutableMapOf<String, Any?>()
    private val privateVariables = mutableSetOf<String>()

    fun setVariable(name: String, value: Any?, isPrivate: Boolean = false) {
        variables[name] = value
        if (isPrivate) {
            privateVariables.add(name)
        }
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }

    fun hasVariable(name: String): Boolean {
        return variables.containsKey(name)
    }

    fun removeVariable(name: String) {
        variables.remove(name)
        privateVariables.remove(name)
    }

    fun clear() {
        variables.clear()
        privateVariables.clear()
    }

    fun getAll(): Map<String, Any?> {
        return variables.filterKeys { !privateVariables.contains(it) }
    }

    fun getAllRaw(): Map<String, Any?> {
        return variables.toMap()
    }

    fun printVariables() {
        val publicVars = variables.filterKeys { !privateVariables.contains(it) }
        println("Variables: $publicVars")
    }
}
