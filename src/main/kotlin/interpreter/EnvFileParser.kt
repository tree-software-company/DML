package interpreter

object EnvFileParser {
    const val MAX_ENV_ENTRIES = 1000

    private val KEY_PATTERN = Regex("[A-Za-z_][A-Za-z0-9_]*")
    private val INLINE_COMMENT = Regex("""\s#.*$""")

    fun parse(content: String): Map<String, String> {
        val entries = linkedMapOf<String, String>()

        for ((index, rawLine) in content.removePrefix("﻿").lines().withIndex()) {
            val lineNumber = index + 1
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith("#")) {
                continue
            }

            val declaration = line.removePrefix("export ").trimStart()
            val separator = declaration.indexOf('=')
            if (separator <= 0) {
                throw invalidSyntax(lineNumber)
            }

            val key = declaration.substring(0, separator).trim()
            if (!KEY_PATTERN.matches(key)) {
                throw invalidSyntax(lineNumber)
            }

            val value = parseValue(declaration.substring(separator + 1), lineNumber)
            if (value.length > DMLExecutor.MAX_INPUT_LENGTH) {
                throw RuntimeException("Invalid .env value at line $lineNumber: longer than ${DMLExecutor.MAX_INPUT_LENGTH} characters")
            }

            entries[key] = value
            if (entries.size > MAX_ENV_ENTRIES) {
                throw RuntimeException("Invalid .env file: more than $MAX_ENV_ENTRIES entries")
            }
        }

        return entries
    }

    private fun parseValue(raw: String, lineNumber: Int): String {
        val value = raw.trimStart()
        return when {
            value.startsWith("\"") -> parseDoubleQuoted(value, lineNumber)
            value.startsWith("'") -> parseSingleQuoted(value, lineNumber)
            else -> raw.replace(INLINE_COMMENT, "").trim()
        }
    }

    private fun parseDoubleQuoted(raw: String, lineNumber: Int): String {
        val builder = StringBuilder()
        var index = 1
        while (index < raw.length) {
            val char = raw[index]
            when {
                char == '\\' && index + 1 < raw.length -> {
                    builder.append(unescape(raw[index + 1]))
                    index += 2
                }
                char == '"' -> {
                    requireOnlyCommentAfter(raw.substring(index + 1), lineNumber)
                    return builder.toString()
                }
                else -> {
                    builder.append(char)
                    index++
                }
            }
        }
        throw invalidSyntax(lineNumber)
    }

    private fun parseSingleQuoted(raw: String, lineNumber: Int): String {
        val end = raw.indexOf('\'', 1)
        if (end < 0) {
            throw invalidSyntax(lineNumber)
        }
        requireOnlyCommentAfter(raw.substring(end + 1), lineNumber)
        return raw.substring(1, end)
    }

    private fun requireOnlyCommentAfter(rest: String, lineNumber: Int) {
        val trimmed = rest.trim()
        if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
            throw invalidSyntax(lineNumber)
        }
    }

    private fun unescape(char: Char): Char {
        return when (char) {
            'n' -> '\n'
            't' -> '\t'
            'r' -> '\r'
            else -> char
        }
    }

    private fun invalidSyntax(lineNumber: Int): RuntimeException {
        return RuntimeException("Invalid .env syntax at line $lineNumber")
    }
}
