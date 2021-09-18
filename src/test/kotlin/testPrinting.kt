import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestPrinting {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    @Test
    fun testSplitChangedStrings() {
        val listToSplit = listOf(
            ChangedString("a", Changes.DELETED),
            ChangedString("b", Changes.DELETED),
            ChangedString("z", Changes.ADDED),
            ChangedString("e", Changes.ADDED),
            ChangedString("c", Changes.UNCHANGED),
            ChangedString("d", Changes.UNCHANGED),
            ChangedString("k", Changes.ADDED),
            ChangedString("e", Changes.UNCHANGED),
            ChangedString("f", Changes.DELETED)
        )
        val actual = splitChangedStrings(listToSplit)
        val expected = listOf(
            PrintingBlock(
                deleted = mutableListOf("a", "b"),
                added = mutableListOf("z", "e"),
                changes = Changes.CHANGED
            ),
            PrintingBlock(
                unchanged = mutableListOf("c", "d"),
                changes = Changes.UNCHANGED
            ),
            PrintingBlock(
                added = mutableListOf("k"),
                changes = Changes.ADDED
            ),
            PrintingBlock(
                unchanged = mutableListOf("e"),
                changes = Changes.UNCHANGED
            ),
            PrintingBlock(
                deleted = mutableListOf("f"),
                changes = Changes.DELETED
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun stSplitChangedStrings_onlyAdded() {
        val listToSplit = listOf(
            ChangedString("z", Changes.ADDED),
            ChangedString("e", Changes.ADDED),
            ChangedString("k", Changes.ADDED),
        )
        val actual = splitChangedStrings(listToSplit)
        val expected = listOf(
            PrintingBlock(
                added = mutableListOf("z", "e", "k"),
                changes = Changes.ADDED
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun stSplitChangedStrings_onlyDeleted() {
        val listToSplit = listOf(
            ChangedString("z", Changes.DELETED),
            ChangedString("e", Changes.DELETED),
            ChangedString("k", Changes.DELETED),
        )
        val actual = splitChangedStrings(listToSplit)
        val expected = listOf(
            PrintingBlock(
                deleted = mutableListOf("z", "e", "k"),
                changes = Changes.DELETED
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun stSplitChangedStrings_onlyUnchanged() {
        val listToSplit = listOf(
            ChangedString("z", Changes.UNCHANGED),
            ChangedString("e", Changes.UNCHANGED),
            ChangedString("k", Changes.UNCHANGED),
        )
        val actual = splitChangedStrings(listToSplit)
        val expected = listOf(
            PrintingBlock(
                unchanged = mutableListOf("z", "e", "k"),
                changes = Changes.UNCHANGED
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testPrintingBlockPrint_deleted() {
        val block = PrintingBlock(deleted = mutableListOf("line1", "line2"), changes = Changes.DELETED)
        block.print(1, 1)
        val expected = """
            1,2d0
            < line1
            < line2
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintingBlockPrint_added() {
        val block = PrintingBlock(added = mutableListOf("string1", "string2"), changes = Changes.ADDED)
        block.print(3, 10)
        val expected = """
            2a10,11
            > string1
            > string2
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintingBlockPrint_unchanged() {
        val block = PrintingBlock(unchanged = mutableListOf("str1", "str2"), changes = Changes.UNCHANGED)
        block.print(2, 3)
        val expected = ""
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintingBlockPrint_changed() {
        val block = PrintingBlock(
            deleted = mutableListOf("str1", "str2", "str3"),
            added = mutableListOf("line1", "line2"),
            changes = Changes.CHANGED
        )
        block.print(4, 2)
        val expected = """
            4,6c2,3
            < str1
            < str2
            < str3
            ---
            > line1
            > line2
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintResult() {
        val changesToPrint = listOf(
            ChangedString("a", Changes.DELETED),
            ChangedString("b", Changes.DELETED),
            ChangedString("z", Changes.ADDED),
            ChangedString("e", Changes.ADDED),
            ChangedString("c", Changes.UNCHANGED),
            ChangedString("d", Changes.UNCHANGED),
            ChangedString("k", Changes.ADDED),
            ChangedString("e", Changes.UNCHANGED),
            ChangedString("f", Changes.DELETED)
        )
        printResult(changesToPrint, Arguments())
        val expected = """
            1,2c1,2
            < a
            < b
            ---
            > z
            > e
            4a5
            > k
            6d6
            < f
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintResult_onlyUnchanged() {
        val changesToPrint = listOf(
            ChangedString("c", Changes.UNCHANGED),
            ChangedString("d", Changes.UNCHANGED),
            ChangedString("e", Changes.UNCHANGED),
        )
        printResult(changesToPrint, Arguments())
        val expected = ""
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintResult_onlyAdded() {
        val changesToPrint = listOf(
            ChangedString("c", Changes.ADDED),
            ChangedString("d", Changes.ADDED),
            ChangedString("e", Changes.ADDED),
        )
        printResult(changesToPrint, Arguments())
        val expected = """
            0a1,3
            > c
            > d
            > e
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintResult_onlyDeleted() {
        val changesToPrint = listOf(
            ChangedString("c", Changes.DELETED),
            ChangedString("d", Changes.DELETED),
            ChangedString("e", Changes.DELETED),
        )
        printResult(changesToPrint, Arguments())
        val expected = """
            1,3d0
            < c
            < d
            < e
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }

    @Test
    fun testPrintResult_deletedAndAdded() {
        val changesToPrint = listOf(
            ChangedString("a", Changes.DELETED),
            ChangedString("b", Changes.DELETED),
            ChangedString("c", Changes.ADDED),
            ChangedString("d", Changes.ADDED),
            ChangedString("e", Changes.ADDED),
        )
        printResult(changesToPrint, Arguments())
        val expected = """
            1,2c1,3
            < a
            < b
            ---
            > c
            > d
            > e
        """.trimIndent()
        assertEquals(expected, stream.toString().trim())
    }
}