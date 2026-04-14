import interpreter.DMLInterpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DMLInterpreterSecurityTest {

    private val interpreter = DMLInterpreter()

    // --- XXE: convertXmlToMap ---

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

    // --- XXE: convertPlistToMap ---

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
}
