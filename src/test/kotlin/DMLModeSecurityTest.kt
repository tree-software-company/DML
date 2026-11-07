import interpreter.DMLExecutor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import parser.DMLLexer
import parser.DMLParser
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DMLModeSecurityTest {

    private fun execute(dir: Path, program: String, mode: String?): DMLExecutor {
        val mainFile = dir.resolve("main.dml").toFile()
        mainFile.writeText(program)
        val executor = DMLExecutor()
        if (mode != null) {
            executor.selectMode(mode)
        }
        executor.executeFile(mainFile)
        return executor
    }

    private fun syntaxErrors(code: String): Int {
        val parser = DMLParser(CommonTokenStream(DMLLexer(CharStreams.fromString(code))))
        parser.removeErrorListeners()
        parser.file()
        return parser.numberOfSyntaxErrors
    }

    @Test
    fun `niewybrany blok nie wykonuje assert loadEnv import ani env`(@TempDir tempDir: Path) {
        val program = """
            mode dev {
                assert 1 == 2;
                loadEnv("../../etc/passwd");
                import "/etc/passwd";
                string missing = env("MISSING");
            }
            string ok = "yes";
        """.trimIndent()

        val symbols = execute(tempDir, program, "prod").getSymbolTable()

        assertEquals("yes", symbols["ok"])
    }

    @Test
    fun `bez wybranego trybu bloki z niebezpiecznymi instrukcjami sa pomijane`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                loadEnv("../../etc/passwd");
                import "/etc/passwd";
            }
            string ok = "yes";
        """.trimIndent()

        assertEquals("yes", execute(tempDir, program, null).getSymbolTable()["ok"])
    }

    @Test
    fun `wybrany blok wykonuje assert`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                assert 1 == 2;
            }
        """.trimIndent()

        assertThrows<AssertionError> {
            execute(tempDir, program, "prod")
        }
    }

    @Test
    fun `loadEnv w wybranym bloku z traversal blokuje dostep poza sandbox`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                loadEnv("../../etc/passwd");
            }
        """.trimIndent()

        val ex = assertThrows<SecurityException> {
            execute(tempDir, program, "prod")
        }

        assertTrue(ex.message?.contains("traversal") == true, "got: ${ex.message}")
    }

    @Test
    fun `import w wybranym bloku z absolutna sciezka poza sandbox jest blokowany`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                import "/etc/passwd";
            }
        """.trimIndent()

        val ex = assertThrows<RuntimeException> {
            execute(tempDir, program, "prod")
        }

        assertTrue(
            ex.message?.contains("traversal") == true || ex.cause?.message?.contains("traversal") == true,
            "got: ${ex.message}"
        )
    }

    @Test
    fun `nazwa trybu spoza dozwolonego wzorca jest odrzucona bez echa wartosci`() {
        val invalidNames = listOf("../x", "a b", "", "1abc", "dev;rm", "dev\nprod", "a".repeat(65))

        invalidNames.forEach { name ->
            val ex = assertThrows<IllegalArgumentException> {
                DMLExecutor().selectMode(name)
            }
            if (name.isNotEmpty()) {
                assertFalse(ex.message?.contains(name) == true, "got: ${ex.message}")
            }
        }
    }

    @Test
    fun `poprawne nazwy trybow sa przyjmowane`() {
        listOf("dev", "prod", "_x", "staging2", "a".repeat(64)).forEach { name ->
            DMLExecutor().selectMode(name)
        }
    }

    @Test
    fun `komunikat o brakujacym kluczu env w wybranym bloku nie zawiera wartosci z pliku`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("API_KEY=super-secret-value\n")
        val program = """
            mode prod {
                loadEnv(".env");
                string x = env("OTHER");
            }
        """.trimIndent()

        val ex = assertThrows<RuntimeException> {
            execute(tempDir, program, "prod")
        }

        assertTrue(ex.message?.contains("OTHER") == true, "got: ${ex.message}")
        assertFalse(ex.message?.contains("super-secret-value") == true, "got: ${ex.message}")
    }

    @Test
    fun `blok mode na najwyzszym poziomie jest poprawny skladniowo`() {
        assertEquals(0, syntaxErrors("""mode dev { string db = "localhost"; }"""))
        assertEquals(0, syntaxErrors("mode dev { }"))
    }

    @Test
    fun `zagniezdzony blok mode jest bledem skladni`() {
        assertTrue(syntaxErrors("""mode a { mode b { string x = "1"; } }""") > 0)
    }

    @Test
    fun `blok mode wewnatrz funkcji jest bledem skladni`() {
        assertTrue(syntaxErrors("""function f() { mode a { string x = "1"; } }""") > 0)
    }

    @Test
    fun `mode jest slowem zastrzezonym i nie moze byc nazwa zmiennej`() {
        assertTrue(syntaxErrors("""string mode = "x";""") > 0)
    }
}
