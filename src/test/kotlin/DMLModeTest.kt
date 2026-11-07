import interpreter.DMLExecutor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DMLModeTest {

    private val devAndProd = """
        mode dev {
            string db = "localhost";
        }

        mode prod {
            string db = "prod.db.server";
        }
    """.trimIndent()

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

    @Test
    fun `wybrany tryb prod ustawia zmienna z bloku prod`(@TempDir tempDir: Path) {
        val symbols = execute(tempDir, devAndProd, "prod").getSymbolTable()

        assertEquals("prod.db.server", symbols["db"])
    }

    @Test
    fun `wybrany tryb dev ustawia zmienna z bloku dev`(@TempDir tempDir: Path) {
        val symbols = execute(tempDir, devAndProd, "dev").getSymbolTable()

        assertEquals("localhost", symbols["db"])
    }

    @Test
    fun `bez wybranego trybu wszystkie bloki sa pomijane`(@TempDir tempDir: Path) {
        val symbols = execute(tempDir, devAndProd, null).getSymbolTable()

        assertFalse(symbols.containsKey("db"))
    }

    @Test
    fun `tryb bez pasujacego bloku nie powoduje bledu i pomija bloki`(@TempDir tempDir: Path) {
        val symbols = execute(tempDir, devAndProd, "staging").getSymbolTable()

        assertFalse(symbols.containsKey("db"))
    }

    @Test
    fun `deklaracje poza blokami wykonuja sie w kazdym trybie`(@TempDir tempDir: Path) {
        val program = """
            string app = "tree";
            $devAndProd
        """.trimIndent()

        assertEquals("tree", execute(tempDir, program, null).getSymbolTable()["app"])
        assertEquals("tree", execute(tempDir, program, "prod").getSymbolTable()["app"])
    }

    @Test
    fun `blok nadpisuje wczesniejsza deklaracje`(@TempDir tempDir: Path) {
        val program = """
            string db = "base";
            mode prod {
                string db = "prod.db.server";
            }
        """.trimIndent()

        assertEquals("prod.db.server", execute(tempDir, program, "prod").getSymbolTable()["db"])
        assertEquals("base", execute(tempDir, program, "dev").getSymbolTable()["db"])
    }

    @Test
    fun `pozniejsza deklaracja nadpisuje blok`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                string db = "prod.db.server";
            }
            string db = "after";
        """.trimIndent()

        assertEquals("after", execute(tempDir, program, "prod").getSymbolTable()["db"])
    }

    @Test
    fun `dwa bloki o tej samej nazwie wykonuja sie po kolei`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                string first = "1";
            }
            mode prod {
                string second = "2";
            }
        """.trimIndent()

        val symbols = execute(tempDir, program, "prod").getSymbolTable()

        assertEquals("1", symbols["first"])
        assertEquals("2", symbols["second"])
    }

    @Test
    fun `instrukcje w bloku widza zmienne zadeklarowane wczesniej w tym samym bloku`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                string host = "prod";
                string address = host + ".server";
            }
        """.trimIndent()

        assertEquals("prod.server", execute(tempDir, program, "prod").getSymbolTable()["address"])
    }

    @Test
    fun `zmienna private z bloku jest ukryta w wyniku`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                private string apiKey = "k";
            }
        """.trimIndent()

        val executor = execute(tempDir, program, "prod")

        assertFalse(executor.getSymbolTable().containsKey("apiKey"))
        assertEquals("k", executor.getAllRaw()["apiKey"])
    }

    @Test
    fun `funkcja zadeklarowana w wybranym bloku jest dostepna`(@TempDir tempDir: Path) {
        val program = """
            mode prod {
                function host(prefix) { return prefix + ".db.server"; }
            }
            string db = host("prod");
        """.trimIndent()

        assertEquals("prod.db.server", execute(tempDir, program, "prod").getSymbolTable()["db"])
    }

    @Test
    fun `importowany plik respektuje tryb wybrany w pliku glownym`(@TempDir tempDir: Path) {
        tempDir.resolve("config.dml").toFile().writeText(devAndProd)

        val program = """import "config.dml";"""

        assertEquals("prod.db.server", execute(tempDir, program, "prod").getSymbolTable()["db"])
        assertEquals("localhost", execute(tempDir, program, "dev").getSymbolTable()["db"])
        assertTrue(execute(tempDir, program, null).getSymbolTable().isEmpty())
    }
}
