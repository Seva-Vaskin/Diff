import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestDiff {
    private val fewStrings: String = "src/test/files/fewStrings.txt"
    private val textFromWikipediaOriginal: String = "src/test/files/textFromWikipediaOriginal.txt"
    private val textFromWikipediaNew: String = "src/test/files/textFromWikipediaNew.txt"
    private val emptyFile: String = "src/test/files/emptyFile.txt"
    private val smallTestFromWikipediaOriginal: String = "src/test/files/smallTestFromWikipediaOriginal.txt"
    private val smallTestFromWikipediaNew: String = "src/test/files/smallTestFromWikipediaNew.txt"

    @Test
    fun testFileToList() {
        val expected = listOf("1st string", "2nd string", "text", "", "end of file")
        val actual = fileToList(fewStrings)
        assertEquals(expected, actual)
    }

    @Test
    fun testFileToList_emptyFile() {
        val expected = listOf<String>()
        val actual = fileToList(emptyFile)
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifference_sameLists() {
        val list = listOf("a", "b", "c")
        val actual = findDifference(list, list)
        val expected = listOf(
            ChangedString("a", Changes.UNCHANGED),
            ChangedString("b", Changes.UNCHANGED),
            ChangedString("c", Changes.UNCHANGED)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifference() {
        val list1 = listOf("a", "b", "c", "d", "e", "f")
        val list2 = listOf("z", "e", "c", "d", "k", "e")
        val actual = findDifference(list1, list2)
        val expected = listOf(
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
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifference_wikipediaText() {
        val list1 = fileToList(textFromWikipediaOriginal)
        val list2 = fileToList(textFromWikipediaNew)
        val actual = findDifference(list1, list2)
        val expected = listOf(
            ChangedString("This is an important", Changes.ADDED),
            ChangedString("notice! It should", Changes.ADDED),
            ChangedString("therefore be located at", Changes.ADDED),
            ChangedString("the beginning of this", Changes.ADDED),
            ChangedString("document!", Changes.ADDED),
            ChangedString("", Changes.ADDED),
            ChangedString("This part of the", Changes.UNCHANGED),
            ChangedString("document has stayed the", Changes.UNCHANGED),
            ChangedString("same from version to", Changes.UNCHANGED),
            ChangedString("version.  It shouldn't", Changes.UNCHANGED),
            ChangedString("be shown if it doesn't", Changes.UNCHANGED),
            ChangedString("change.  Otherwise, that", Changes.UNCHANGED),
            ChangedString("would not be helping to", Changes.UNCHANGED),
            ChangedString("compress the size of the", Changes.UNCHANGED),
            ChangedString("changes.", Changes.UNCHANGED),
            ChangedString("", Changes.DELETED),
            ChangedString("This paragraph contains", Changes.DELETED),
            ChangedString("text that is outdated.", Changes.DELETED),
            ChangedString("It will be deleted in the", Changes.DELETED),
            ChangedString("near future.", Changes.DELETED),
            ChangedString("", Changes.UNCHANGED),
            ChangedString("It is important to spell", Changes.UNCHANGED),
            ChangedString("check this dokument. On", Changes.DELETED),
            ChangedString("check this document. On", Changes.ADDED),
            ChangedString("the other hand, a", Changes.UNCHANGED),
            ChangedString("misspelled word isn't", Changes.UNCHANGED),
            ChangedString("the end of the world.", Changes.UNCHANGED),
            ChangedString("Nothing in the rest of", Changes.UNCHANGED),
            ChangedString("this paragraph needs to", Changes.UNCHANGED),
            ChangedString("be changed. Things can", Changes.UNCHANGED),
            ChangedString("be added after it.", Changes.UNCHANGED),
            ChangedString("", Changes.ADDED),
            ChangedString("This paragraph contains", Changes.ADDED),
            ChangedString("important new additions", Changes.ADDED),
            ChangedString("to this document.", Changes.ADDED)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testFindDifference_smallTestFromWikipedia() {
        val list1 = fileToList(smallTestFromWikipediaOriginal)
        val list2 = fileToList(smallTestFromWikipediaNew)
        val actual = findDifference(list1, list2)
        val expected = listOf(
            ChangedString("a", Changes.UNCHANGED),
            ChangedString("b", Changes.UNCHANGED),
            ChangedString("c", Changes.UNCHANGED),
            ChangedString("d", Changes.UNCHANGED),
            ChangedString("e", Changes.ADDED),
            ChangedString("f", Changes.UNCHANGED),
            ChangedString("g", Changes.UNCHANGED),
            ChangedString("h", Changes.DELETED),
            ChangedString("i", Changes.ADDED),
            ChangedString("j", Changes.UNCHANGED),
            ChangedString("q", Changes.DELETED),
            ChangedString("k", Changes.ADDED),
            ChangedString("r", Changes.ADDED),
            ChangedString("x", Changes.ADDED),
            ChangedString("y", Changes.ADDED),
            ChangedString("z", Changes.UNCHANGED)
        )
        assertEquals(expected, actual)
    }
}