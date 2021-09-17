import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestDiff {
    private val fewStrings: String = "src/test/files/fewStrings.txt"

    @Test
    fun testFileToList() {
        val expected = listOf("1st string", "2nd string", "text", "", "end of file")
        val actual = fileToList(fewStrings)
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifferenceSameLists() {
        val list = listOf("a", "b", "c")
        val actual = findDifference(list, list)
        val expected = listOf(ChangedString("a", Changes.UNCHANGED),
            ChangedString("b", Changes.UNCHANGED), ChangedString("c", Changes.UNCHANGED))
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifference() {
        val list1 = listOf("a", "b", "c", "d", "e", "f")
        val list2 = listOf("z", "e", "c", "d", "k", "e")
        val actual = findDifference(list1, list2)
        val expected = listOf(ChangedString("a", Changes.DELETED),
            ChangedString("b", Changes.DELETED), ChangedString("z", Changes.ADDED),
            ChangedString("e", Changes.ADDED), ChangedString("c", Changes.UNCHANGED),
            ChangedString("d", Changes.UNCHANGED), ChangedString("k", Changes.ADDED),
            ChangedString("e", Changes.UNCHANGED), ChangedString("f", Changes.DELETED))
        assertEquals(expected, actual)
    }
}