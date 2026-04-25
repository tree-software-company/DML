import interpreter.DMLInterpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DMLInterpreterSecurityTest {

    private val interpreter = DMLInterpreter()

    @Test
    fun `convertXmlToMap blokuje XXE z external entity`() {
        val xxeXml = """
            <?xml version="1.0"?>
            <!DOCTYPE foo [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
            <dml>
              <secret>&xxe;</secret>
            </dml>
        """.trimIndent()

        assertThrows<Exception> {
            interpreter.convertXmlToMap(xxeXml)
        }
    }

    @Test
    fun `convertXmlToMap blokuje XXE z zewnetrznym URL`() {
        val ssrfXml = """
            <?xml version="1.0"?>
            <!DOCTYPE foo [<!ENTITY xxe SYSTEM "http://169.254.169.254/latest/meta-data/">]>
            <dml>
              <token>&xxe;</token>
            </dml>
        """.trimIndent()

        assertThrows<Exception> {
            interpreter.convertXmlToMap(ssrfXml)
        }
    }

    @Test
    fun `convertXmlToMap poprawnie parsuje zwykly XML`() {
        val validXml = """
            <?xml version="1.0"?>
            <dml>
              <name>Alice</name>
              <age>30</age>
              <active>true</active>
            </dml>
        """.trimIndent()

        val result = interpreter.convertXmlToMap(validXml)

        assertEquals("Alice", result["name"])
        assertEquals(30, result["age"])
        assertEquals(true, result["active"])
    }


    @Test
    fun `convertPlistToMap blokuje XXE z external entity`() {
        val xxePlist = """
            <?xml version="1.0"?>
            <!DOCTYPE plist [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
            <plist version="1.0">
              <dict>
                <key>secret</key>
                <string>&xxe;</string>
              </dict>
            </plist>
        """.trimIndent()

        assertThrows<Exception> {
            interpreter.convertPlistToMap(xxePlist)
        }
    }

    @Test
    fun `convertPlistToMap poprawnie parsuje zwykly plist`() {
        val validPlist = """
            <?xml version="1.0"?>
            <plist version="1.0">
              <dict>
                <key>name</key>
                <string>Bob</string>
                <key>score</key>
                <integer>42</integer>
                <key>enabled</key>
                <true/>
              </dict>
            </plist>
        """.trimIndent()

        val result = interpreter.convertPlistToMap(validPlist)

        assertEquals("Bob", result["name"])
        assertEquals(42, result["score"])
        assertEquals(true, result["enabled"])
    }

    @Test
    fun `regex blokuje zbyt dlugi wzorzec`() {
        val longPattern = "a".repeat(501)
        assertThrows<SecurityException> {
            interpreter.executeString("""
                regex bad = "$longPattern";
            """.trimIndent())
        }
    }

    @Test
    fun `regex blokuje zbyt dlugi input`() {
        val longInput = "a".repeat(10_001)
        assertThrows<SecurityException> {
            interpreter.executeString("""
                regex p = ".*";
                string s = "$longInput";
                validate s matches p;
            """.trimIndent())
        }
    }

    @Test
    fun `regex nie przekracza timeout dla normalnych wzorow`() {
        interpreter.executeString("""
            regex digits = "[0-9]+";
            string n = "1234567890";
            validate n matches digits;
        """.trimIndent())
    }

    @Test
    fun `regex dziala poprawnie dla bezpiecznych wzorow`() {
        interpreter.executeString("""
            regex email = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}";
            string addr = "user@example.com";
            validate addr matches email;
        """.trimIndent())
    }
}
