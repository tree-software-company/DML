import cli.Cli
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DMLModeCliTest {

    private val originalOut = System.out
    private val captured = ByteArrayOutputStream()

    @BeforeEach
    fun captureOutput() {
        System.setOut(PrintStream(captured, true))
    }

    @AfterEach
    fun restoreOutput() {
        System.setOut(originalOut)
    }

    private fun runCli(vararg args: String): String {
        captured.reset()
        Cli().run(arrayOf(*args))
        return captured.toString()
    }

    private fun writeProgram(dir: Path): String {
        val file = dir.resolve("app.dml").toFile()
        file.writeText(
            """
            mode dev {
                string db = "localhost";
            }

            mode prod {
                string db = "prod.db.server";
            }
            """.trimIndent()
        )
        return file.absolutePath
    }

    @Test
    fun `flaga mode po sciezce pliku wybiera blok prod`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir), "--mode", "prod")

        assertTrue(output.contains("db = prod.db.server"), "got: $output")
        assertFalse(output.contains("localhost"), "got: $output")
    }

    @Test
    fun `flaga mode ze znakiem rownosci wybiera blok prod`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir), "--mode=prod")

        assertTrue(output.contains("db = prod.db.server"), "got: $output")
    }

    @Test
    fun `skrot m przed komenda wybiera blok dev`(@TempDir tempDir: Path) {
        val output = runCli("-m", "dev", "-r", "dml", writeProgram(tempDir))

        assertTrue(output.contains("db = localhost"), "got: $output")
        assertFalse(output.contains("prod.db.server"), "got: $output")
    }

    @Test
    fun `flaga mode bez komendy dziala jak tryb tekstowy`(@TempDir tempDir: Path) {
        val output = runCli("--mode", "prod", writeProgram(tempDir))

        assertTrue(output.contains("db = prod.db.server"), "got: $output")
    }

    @Test
    fun `bez flagi mode bloki sa pomijane`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir))

        assertFalse(output.contains("db ="), "got: $output")
    }

    @Test
    fun `zapis json z flaga mode zawiera zmienna z wybranego bloku`(@TempDir tempDir: Path) {
        val output = runCli("-w", "json", writeProgram(tempDir), "--mode", "prod")

        assertTrue(output.contains("prod.db.server"), "got: $output")
        assertFalse(output.contains("localhost"), "got: $output")
    }

    @Test
    fun `flaga mode bez wartosci konczy sie komunikatem bledu`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir), "--mode")

        assertTrue(output.contains("Error:") && output.contains("requires a mode name"), "got: $output")
    }

    @Test
    fun `flaga mode podana dwa razy konczy sie komunikatem bledu`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir), "--mode", "dev", "-m", "prod")

        assertTrue(output.contains("Error:") && output.contains("only once"), "got: $output")
        assertFalse(output.contains("db ="), "got: $output")
    }

    @Test
    fun `nieprawidlowa nazwa trybu konczy sie komunikatem bledu bez echa wartosci`(@TempDir tempDir: Path) {
        val output = runCli("-r", "dml", writeProgram(tempDir), "--mode", "../x")

        assertTrue(output.contains("Error: Invalid mode name"), "got: $output")
        assertFalse(output.contains("../x"), "got: $output")
        assertFalse(output.contains("db ="), "got: $output")
    }

    @Test
    fun `flaga mode z konwersja json jest odrzucona`(@TempDir tempDir: Path) {
        val jsonFile = tempDir.resolve("data.json").toFile()
        jsonFile.writeText("""{"db": "x"}""")

        val output = runCli("-r", "json", jsonFile.absolutePath, "--mode", "prod")

        assertTrue(output.contains("Error:") && output.contains("works only"), "got: $output")
        assertFalse(output.contains("db ="), "got: $output")
    }
}
