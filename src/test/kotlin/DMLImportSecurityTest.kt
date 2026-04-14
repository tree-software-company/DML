import interpreter.DMLInterpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.assertTrue

class DMLImportSecurityTest {

    // --- Path traversal w instrukcji import ---

    @Test
    fun `import z relatywna sciezka traversal blokuje dostep poza sandbox`(@TempDir tempDir: Path) {
        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "../../etc/passwd";""")

        val ex = assertThrows<RuntimeException> {
            DMLInterpreter().executeFile(mainFile.absolutePath)
        }
        assertTrue(
            ex.message?.contains("traversal") == true ||
            ex.cause?.message?.contains("traversal") == true,
            "Oczekiwano komunikatu o path traversal, got: ${ex.message}"
        )
    }

    @Test
    fun `import z absolutna sciezka poza sandbox jest blokowany`(@TempDir tempDir: Path) {
        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "/etc/passwd";""")

        val ex = assertThrows<RuntimeException> {
            DMLInterpreter().executeFile(mainFile.absolutePath)
        }
        assertTrue(
            ex.message?.contains("traversal") == true ||
            ex.cause?.message?.contains("traversal") == true,
            "Oczekiwano komunikatu o path traversal, got: ${ex.message}"
        )
    }

    @Test
    fun `import z zagniezdzona sciezka canonical traversal jest blokowany`(@TempDir tempDir: Path) {
        val subdir = tempDir.resolve("subdir").toFile().also { it.mkdir() }
        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "subdir/../../../etc/passwd";""")

        val ex = assertThrows<RuntimeException> {
            DMLInterpreter().executeFile(mainFile.absolutePath)
        }
        assertTrue(
            ex.message?.contains("traversal") == true ||
            ex.cause?.message?.contains("traversal") == true,
            "Oczekiwano komunikatu o path traversal, got: ${ex.message}"
        )
    }

    @Test
    fun `import poprawnego pliku w tym samym katalogu dziala`(@TempDir tempDir: Path) {
        val sibling = tempDir.resolve("config.dml").toFile()
        sibling.writeText("""string appName = "TestApp";""")

        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "./config.dml";""")

        // Nie powinno rzucic wyjatku
        DMLInterpreter().executeFile(mainFile.absolutePath)
    }

    @Test
    fun `import pliku w podkatalogu dziala`(@TempDir tempDir: Path) {
        val nested = tempDir.resolve("nested").toFile().also { it.mkdir() }
        val innerFile = nested.resolve("inner.dml")
        innerFile.writeText("""number version = 1;""")

        val mainFile = tempDir.resolve("main.dml").toFile()
        mainFile.writeText("""import "nested/inner.dml";""")

        // Nie powinno rzucic wyjatku
        DMLInterpreter().executeFile(mainFile.absolutePath)
    }
}
