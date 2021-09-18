import kotlin.test.*

internal class TestArgumentsParser {
    private val simpleText1: String = "src/test/files/simpleText1.txt"
    private val simpleText2: String = "src/test/files/simpleText2.txt"
    private val fileDoesNotExist: String = "src/test/files/fileDoesNotExist.txt"

    @Test
    fun testParseArguments_onlyNames() {
        val arguments = parseArguments(arrayOf(simpleText1, simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2), arguments)
    }

    @Test
    fun testParseArguments_help() {
        val arguments = parseArguments(arrayOf("--help"))
        assertEquals(Arguments(help = true), arguments)
    }

    @Test
    fun testParseArguments_namesAndHelp() {
        var arguments = parseArguments(arrayOf("--help", simpleText1, simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)

        arguments = parseArguments(arrayOf(simpleText1, "--help", simpleText2))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)

        arguments = parseArguments(arrayOf(simpleText1, simpleText2, "--help"))
        assertEquals(Arguments(simpleText1, simpleText2, true), arguments)
    }

    @Test
    fun testParseArguments_fileDoesNotExist() {
        assertFails {
            parseArguments(arrayOf(fileDoesNotExist, simpleText1))
        }
        assertFails {
            parseArguments(arrayOf(simpleText1, fileDoesNotExist))
        }
        assertEquals(
            Arguments(simpleText1, fileDoesNotExist, true),
            parseArguments(arrayOf(simpleText1, fileDoesNotExist, "--help"))
        )
    }

    @Test
    fun testParseArguments_extraOperand() {
        assertFails {
            parseArguments(arrayOf(simpleText1, simpleText2, fileDoesNotExist))
        }
    }

    @Test
    fun testParseArguments_missingOperand() {
        assertFails {
            parseArguments(arrayOf(simpleText1))
        }
    }
}