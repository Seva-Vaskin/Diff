import kotlin.test.*

internal class TestArgumentsParser {
    private val name1Path: String = "src/test/files/simpleText1.txt"
    private val name2Path: String = "src/test/files/simpleText2.txt"

    @Test
    fun testParseArgumentsNames() {
        val arguments = parseArguments(arrayOf(name1Path, name2Path))
        assertEquals(Arguments(name1Path, name2Path), arguments)
    }

    @Test
    fun testParseArgumentsHelp() {
        val arguments = parseArguments(arrayOf("--help"))
        assertEquals(Arguments(help = true), arguments)
    }

    @Test
    fun testParseArgumentsNamesAndHelp() {
        var arguments = parseArguments(arrayOf("--help", name1Path, name2Path))
        assertEquals(Arguments(name1Path, name2Path, true), arguments)

        arguments = parseArguments(arrayOf(name1Path, "--help", name2Path))
        assertEquals(Arguments(name1Path, name2Path, true), arguments)

        arguments = parseArguments(arrayOf(name1Path, name2Path, "--help"))
        assertEquals(Arguments(name1Path, name2Path, true), arguments)
    }
}