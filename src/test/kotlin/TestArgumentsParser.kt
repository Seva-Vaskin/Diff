import kotlin.test.*

internal class TestArgumentsParser {
    private val simpleText1: String = "src/test/files/simpleText1.txt"
    private val simpleText2: String = "src/test/files/simpleText2.txt"
    private val fileDoesNotExist: String = "src/test/files/fileDoesNotExist.txt"

    @Test
    fun testParseArgumentsNames() {
        val arguments = parseArguments(arrayOf(simpleText1, simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2), arguments)
    }

    @Test
    fun testParseArgumentsHelp() {
        val arguments = parseArguments(arrayOf("--help"))
        assertEquals(Arguments(help = true), arguments)
    }

    @Test
    fun testParseArgumentsNamesAndHelp() {
        var arguments = parseArguments(arrayOf("--help", simpleText1, simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)

        arguments = parseArguments(arrayOf(simpleText1, "--help", simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)

        arguments = parseArguments(arrayOf(simpleText1, simpleText2, "--help"))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)
    }

    @Test
    fun testParseArgumentsFileDoesNotExist() {
        assertFails {
            parseArguments(arrayOf(fileDoesNotExist, simpleText1))
        }
        assertFails {
            parseArguments(arrayOf(simpleText1, fileDoesNotExist))
        }
        assertEquals(Arguments(simpleText1, fileDoesNotExist, true),
                    parseArguments(arrayOf(simpleText1, fileDoesNotExist, "--help")))
    }
}