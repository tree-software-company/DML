import interpreter.DMLExecutor
import interpreter.DMLInterpreter
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DMLEnvConfigTest {

    private fun runProgram(dir: Path, program: String) {
        val mainFile = dir.resolve("main.dml").toFile()
        mainFile.writeText(program)
        DMLInterpreter().executeFile(mainFile.absolutePath)
    }

    @Test
    fun `loadEnv wczytuje wartosci a env je zwraca`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("DB_HOST=localhost\nDB_PORT=5432\n")

        runProgram(
            tempDir,
            """
            loadEnv(".env");
            assert env("DB_HOST") == "localhost";
            assert env("DB_PORT") == "5432";
            """.trimIndent()
        )
    }

    @Test
    fun `env zwraca wartosc do zmiennej typu string`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("DB_HOST=localhost\n")

        runProgram(
            tempDir,
            """
            loadEnv(".env");
            string host = env("DB_HOST");
            private string secret = env("DB_HOST");
            assert host == "localhost";
            assert secret == "localhost";
            """.trimIndent()
        )
    }

    @Test
    fun `env z wartoscia domyslna uzywa jej tylko gdy klucza brak`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("DB_HOST=localhost\n")

        runProgram(
            tempDir,
            """
            loadEnv(".env");
            assert env("MISSING", "fallback") == "fallback";
            assert env("DB_HOST", "other") == "localhost";
            """.trimIndent()
        )
    }

    @Test
    fun `env z domyslna liczba zwraca liczbe do zmiennej number`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("A=1\n")

        runProgram(
            tempDir,
            """
            loadEnv(".env");
            number port = env("PORT", 8080);
            assert port == 8080;
            """.trimIndent()
        )
    }

    @Test
    fun `drugi loadEnv nadpisuje klucze z pierwszego i zachowuje pozostale`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("A=first\nB=kept\n")
        tempDir.resolve("local.env").toFile().writeText("A=second\n")

        runProgram(
            tempDir,
            """
            loadEnv(".env");
            loadEnv("local.env");
            assert env("A") == "second";
            assert env("B") == "kept";
            """.trimIndent()
        )
    }

    @Test
    fun `env bez wczytanego pliku rzuca wyjatek z nazwa klucza`(@TempDir tempDir: Path) {
        val ex = assertThrows<RuntimeException> {
            runProgram(tempDir, """string x = env("NIE_MA");""")
        }

        assertTrue(ex.message?.contains("NIE_MA") == true, "got: ${ex.message}")
    }

    @Test
    fun `env nie czyta zmiennych srodowiskowych procesu`(@TempDir tempDir: Path) {
        assumeTrue(System.getenv("PATH") != null)
        tempDir.resolve(".env").toFile().writeText("A=1\n")

        val ex = assertThrows<RuntimeException> {
            runProgram(
                tempDir,
                """
                loadEnv(".env");
                string leaked = env("PATH");
                """.trimIndent()
            )
        }

        assertTrue(ex.message?.contains("not defined") == true, "got: ${ex.message}")
    }

    @Test
    fun `komunikat o brakujacym kluczu nie zawiera wartosci z pliku`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("API_KEY=super-secret-value\n")

        val ex = assertThrows<RuntimeException> {
            runProgram(
                tempDir,
                """
                loadEnv(".env");
                string x = env("OTHER");
                """.trimIndent()
            )
        }

        assertFalse(ex.message?.contains("super-secret-value") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv w importowanym pliku rozwiazuje sciezke wzgledem tego pliku`(@TempDir tempDir: Path) {
        val nested = tempDir.resolve("nested").toFile().also { it.mkdir() }
        nested.resolve("local.env").writeText("FROM_NESTED=yes\n")
        nested.resolve("config.dml").writeText("""loadEnv("local.env");""")

        runProgram(
            tempDir,
            """
            import "nested/config.dml";
            assert env("FROM_NESTED") == "yes";
            """.trimIndent()
        )
    }

    @Test
    fun `funkcja uzytkownika o nazwie env ma pierwszenstwo przed wbudowana`(@TempDir tempDir: Path) {
        runProgram(
            tempDir,
            """
            function env(key) { return "custom"; }
            assert env("anything") == "custom";
            """.trimIndent()
        )
    }

    @Test
    fun `nieznana funkcja nadal zglasza blad braku funkcji`(@TempDir tempDir: Path) {
        val ex = assertThrows<RuntimeException> {
            runProgram(tempDir, """unknownFunction("x");""")
        }

        assertTrue(ex.message?.contains("not found") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv z relatywna sciezka traversal blokuje dostep poza sandbox`(@TempDir tempDir: Path) {
        val ex = assertThrows<SecurityException> {
            runProgram(tempDir, """loadEnv("../../etc/passwd");""")
        }

        assertTrue(ex.message?.contains("traversal") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv z absolutna sciezka poza sandbox jest blokowany`(@TempDir tempDir: Path) {
        val ex = assertThrows<SecurityException> {
            runProgram(tempDir, """loadEnv("/etc/passwd");""")
        }

        assertTrue(ex.message?.contains("traversal") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv przez symlink wskazujacy poza sandbox jest blokowany`(
        @TempDir projectDir: Path,
        @TempDir outsideDir: Path
    ) {
        val outsideFile = outsideDir.resolve("secret.env")
        outsideFile.toFile().writeText("SECRET=leaked\n")
        Files.createSymbolicLink(projectDir.resolve("link.env"), outsideFile)

        val ex = assertThrows<SecurityException> {
            runProgram(projectDir, """loadEnv("link.env");""")
        }

        assertTrue(ex.message?.contains("traversal") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv w kodzie bez pliku bez sandboxa rzuca SecurityException`(@TempDir tempDir: Path) {
        val envFile = tempDir.resolve(".env").toFile()
        envFile.writeText("A=1\n")

        assertThrows<SecurityException> {
            DMLInterpreter().executeString("""loadEnv("${envFile.absolutePath}");""")
        }
    }

    @Test
    fun `plik env wiekszy niz limit rzuca SecurityException`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile()
            .writeBytes(ByteArray((DMLExecutor.MAX_FILE_SIZE_BYTES + 1).toInt()))

        val ex = assertThrows<SecurityException> {
            runProgram(tempDir, """loadEnv(".env");""")
        }

        assertTrue(ex.message?.contains("too large") == true, "got: ${ex.message}")
    }

    @Test
    fun `zla linia w pliku env zglasza numer linii bez ujawniania tresci`(@TempDir tempDir: Path) {
        tempDir.resolve(".env").toFile().writeText("this is not valid secret-xyz\n")

        val ex = assertThrows<RuntimeException> {
            runProgram(tempDir, """loadEnv(".env");""")
        }

        assertTrue(ex.message?.contains("line 1") == true, "got: ${ex.message}")
        assertFalse(ex.message?.contains("secret-xyz") == true, "got: ${ex.message}")
    }

    @Test
    fun `brak pliku env daje czytelny blad`(@TempDir tempDir: Path) {
        val ex = assertThrows<RuntimeException> {
            runProgram(tempDir, """loadEnv("nope.env");""")
        }

        assertTrue(ex.message?.contains("File not found") == true, "got: ${ex.message}")
    }

    @Test
    fun `katalog zamiast pliku env daje blad braku pliku`(@TempDir tempDir: Path) {
        tempDir.resolve("dir.env").toFile().mkdir()

        val ex = assertThrows<RuntimeException> {
            runProgram(tempDir, """loadEnv("dir.env");""")
        }

        assertTrue(ex.message?.contains("File not found") == true, "got: ${ex.message}")
    }

    @Test
    fun `laczna liczba kluczy z wielu plikow nie przekracza limitu`(@TempDir tempDir: Path) {
        val limit = interpreter.EnvFileParser.MAX_ENV_ENTRIES
        tempDir.resolve("a.env").toFile().writeText((1..limit).joinToString("\n") { "A$it=v" })
        tempDir.resolve("b.env").toFile().writeText("EXTRA=v")

        val ex = assertThrows<RuntimeException> {
            runProgram(
                tempDir,
                """
                loadEnv("a.env");
                loadEnv("b.env");
                """.trimIndent()
            )
        }

        assertTrue(ex.message?.contains("more than") == true, "got: ${ex.message}")
    }

    @Test
    fun `loadEnv z niepoprawna liczba argumentow lub typem rzuca wyjatek`(@TempDir tempDir: Path) {
        assertThrows<RuntimeException> { runProgram(tempDir, """loadEnv();""") }
        assertThrows<RuntimeException> { runProgram(tempDir, """loadEnv(5);""") }
        assertThrows<RuntimeException> { runProgram(tempDir, """string x = env(5);""") }
        assertThrows<RuntimeException> { runProgram(tempDir, """string x = env("a", "b", "c");""") }
    }
}
