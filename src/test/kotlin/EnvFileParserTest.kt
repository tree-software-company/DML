import interpreter.DMLExecutor
import interpreter.EnvFileParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnvFileParserTest {

    @Test
    fun `parser czyta proste pary klucz wartosc`() {
        val parsed = EnvFileParser.parse("DB_HOST=localhost\nDB_PORT=5432\n")

        assertEquals(mapOf("DB_HOST" to "localhost", "DB_PORT" to "5432"), parsed)
    }

    @Test
    fun `parser pomija puste linie i komentarze`() {
        val parsed = EnvFileParser.parse("# nagłówek\n\n   \nA=1\n  # wciety komentarz\nB=2")

        assertEquals(mapOf("A" to "1", "B" to "2"), parsed)
    }

    @Test
    fun `parser obsluguje prefiks export`() {
        val parsed = EnvFileParser.parse("export APP_NAME=TestApp")

        assertEquals("TestApp", parsed["APP_NAME"])
    }

    @Test
    fun `parser obsluguje cudzyslow z escape sequences`() {
        val parsed = EnvFileParser.parse("GREETING=\"hello world\"\nMULTI=\"line1\\nline2\"\nQUOTED=\"say \\\"hi\\\"\"")

        assertEquals("hello world", parsed["GREETING"])
        assertEquals("line1\nline2", parsed["MULTI"])
        assertEquals("say \"hi\"", parsed["QUOTED"])
    }

    @Test
    fun `parser traktuje apostrofy literalnie i nie rozwija zmiennych`() {
        val parsed = EnvFileParser.parse("RAW='no \${HOME} and \\n here'")

        assertEquals("no \${HOME} and \\n here", parsed["RAW"])
    }

    @Test
    fun `parser nie rozwija zmiennych w wartosciach bez cudzyslowu`() {
        val parsed = EnvFileParser.parse("PATH_COPY=\${HOME}/bin")

        assertEquals("\${HOME}/bin", parsed["PATH_COPY"])
    }

    @Test
    fun `parser ucina komentarz w linii ale zachowuje hash bez spacji`() {
        val parsed = EnvFileParser.parse("TOKEN=abc # komentarz\nCOLOR=#ff0000\nANCHOR=page#top")

        assertEquals("abc", parsed["TOKEN"])
        assertEquals("#ff0000", parsed["COLOR"])
        assertEquals("page#top", parsed["ANCHOR"])
    }

    @Test
    fun `parser traktuje komentarz po znaku rownosci jako pusta wartosc`() {
        val parsed = EnvFileParser.parse("A=   # tylko komentarz")

        assertEquals("", parsed["A"])
    }

    @Test
    fun `parser zachowuje hash wewnatrz cudzyslowu i ucina komentarz po nim`() {
        val parsed = EnvFileParser.parse("A=\"x # y\" # komentarz")

        assertEquals("x # y", parsed["A"])
    }

    @Test
    fun `parser dopuszcza pusta wartosc i znak rownosci w wartosci`() {
        val parsed = EnvFileParser.parse("EMPTY=\nURL=https://x.test/?a=b")

        assertEquals("", parsed["EMPTY"])
        assertEquals("https://x.test/?a=b", parsed["URL"])
    }

    @Test
    fun `parser nadpisuje powtorzony klucz ostatnia wartoscia`() {
        val parsed = EnvFileParser.parse("A=first\nA=second")

        assertEquals("second", parsed["A"])
    }

    @Test
    fun `parser obsluguje koncowki linii windows i BOM`() {
        val parsed = EnvFileParser.parse("﻿A=1\r\nB=2\r\n")

        assertEquals(mapOf("A" to "1", "B" to "2"), parsed)
    }

    @Test
    fun `parser odrzuca linie bez znaku rownosci i podaje numer linii bez tresci`() {
        val ex = assertThrows<RuntimeException> {
            EnvFileParser.parse("OK=1\nthis is not valid secret-xyz")
        }

        assertTrue(ex.message?.contains("line 2") == true, "got: ${ex.message}")
        assertFalse(ex.message?.contains("secret-xyz") == true, "got: ${ex.message}")
    }

    @Test
    fun `parser odrzuca niepoprawny klucz`() {
        assertThrows<RuntimeException> { EnvFileParser.parse("1BAD=value") }
        assertThrows<RuntimeException> { EnvFileParser.parse("BAD KEY=value") }
        assertThrows<RuntimeException> { EnvFileParser.parse("=value") }
    }

    @Test
    fun `parser odrzuca niezamkniety cudzyslow bez ujawniania wartosci`() {
        val ex = assertThrows<RuntimeException> {
            EnvFileParser.parse("A=\"unterminated-secret")
        }

        assertTrue(ex.message?.contains("line 1") == true, "got: ${ex.message}")
        assertFalse(ex.message?.contains("unterminated-secret") == true, "got: ${ex.message}")
    }

    @Test
    fun `parser odrzuca tekst po zamknietym cudzyslowie`() {
        assertThrows<RuntimeException> { EnvFileParser.parse("A=\"x\" trailing") }
        assertThrows<RuntimeException> { EnvFileParser.parse("A='x' trailing") }
    }

    @Test
    fun `parser odrzuca zbyt dlugi plik z liczba wpisow ponad limit`() {
        val content = (1..EnvFileParser.MAX_ENV_ENTRIES + 1).joinToString("\n") { "K$it=v" }

        val ex = assertThrows<RuntimeException> { EnvFileParser.parse(content) }

        assertTrue(ex.message?.contains("more than") == true, "got: ${ex.message}")
    }

    @Test
    fun `parser przyjmuje dokladnie maksymalna liczbe wpisow`() {
        val content = (1..EnvFileParser.MAX_ENV_ENTRIES).joinToString("\n") { "K$it=v" }

        assertEquals(EnvFileParser.MAX_ENV_ENTRIES, EnvFileParser.parse(content).size)
    }

    @Test
    fun `parser odrzuca zbyt dluga wartosc bez jej ujawniania`() {
        val longValue = "a".repeat(DMLExecutor.MAX_INPUT_LENGTH + 1)

        val ex = assertThrows<RuntimeException> { EnvFileParser.parse("BIG=$longValue") }

        assertTrue(ex.message?.contains("line 1") == true, "got: ${ex.message}")
        assertFalse(ex.message?.contains("aaaaaaaaaa") == true, "got: ${ex.message}")
    }
}
