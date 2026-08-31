import interpreter.DMLExecutor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.assertTrue

class DMLFileSizeLimitTest {

    @Test
    fun `plik dml wiekszy niz limit rzuca SecurityException`(@TempDir tempDir: Path) {
        val bigFile = tempDir.resolve("big.dml").toFile()
        bigFile.writeBytes(ByteArray((DMLExecutor.MAX_FILE_SIZE_BYTES + 1).toInt()))

        val ex = assertThrows<SecurityException> {
            DMLExecutor().executeFile(bigFile)
        }
        assertTrue(
            ex.message?.contains("too large") == true,
            "Oczekiwano komunikatu o zbyt duzym pliku, got: ${ex.message}"
        )
    }

    @Test
    fun `import zbyt duzego pliku rzuca SecurityException`(@TempDir tempDir: Path) {
        val bigFile = tempDir.resolve("big.dml").toFile()
        bigFile.writeBytes(ByteArray((DMLExecutor.MAX_FILE_SIZE_BYTES + 1).toInt()))

        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "big.dml";""")

        val ex = assertThrows<RuntimeException> {
            DMLExecutor().executeFile(mainFile)
        }
        assertTrue(
            ex.message?.contains("too large") == true,
            "Oczekiwano komunikatu o zbyt duzym pliku, got: ${ex.message}"
        )
    }

    @Test
    fun `plik dml na granicy limitu wykonuje sie normalnie`(@TempDir tempDir: Path) {
        val code = """string appName = "TestApp";"""
        val padding = " ".repeat((DMLExecutor.MAX_FILE_SIZE_BYTES - code.length).toInt())

        val file = tempDir.resolve("exact.dml").toFile()
        file.writeText(code + padding)

        DMLExecutor().executeFile(file)
    }
}
