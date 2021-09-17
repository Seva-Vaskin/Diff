import java.io.File
import java.lang.Integer.max

/**
 * Describes possible string changes
 */
enum class Changes {
    UNCHANGED, ADDED, DELETED
}

/**
 * Describes which changes was in current string
 */
data class ChangedString(val data: String, val changes: Changes)

/**
 * Returns list of file strings
 */
fun fileToList(fileName: String): List<String> {
    val file = File(fileName)
    return file.readLines()
}

/**
 * Finds the largest common subsequence (lcs) of two lists of strings.
 * Returns list of changes:
 * 1) All strings from lcs tagged as UNCHANGED
 * 2) All string from first list but not from lcs tagged as DELETED
 * 3) All string from second list but not from lcd tagged as ADDED
 */
fun findDifference(firstList: List<String>, secondList: List<String>): List<ChangedString> {
    // find length of the largest common subsequence using dynamic programming
    val lcsLength = MutableList(firstList.size + 1) { MutableList(secondList.size + 1) { 0 } }
    for (i in 1..firstList.size) {
        for (j in 1..secondList.size) {
            lcsLength[i][j] = if (firstList[i - 1] == secondList[j - 1])
                lcsLength[i - 1][j - 1] + 1
            else
                max(lcsLength[i - 1][j], lcsLength[i][j - 1])
        }
    }
    // make result list of changes
    val result = mutableListOf<ChangedString>()
    var i = firstList.size
    var j = secondList.size
    while (i > 0 || j > 0) {
        if (i != 0 && j != 0 && firstList[i - 1] == secondList[j - 1]) {
            result.add(ChangedString(firstList[i - 1], Changes.UNCHANGED))
            i--
            j--
        } else if (i == 0 || j != 0 && lcsLength[i][j] == lcsLength[i][j - 1]) {
            result.add(ChangedString(secondList[j - 1], Changes.ADDED))
            j--
        } else if (j == 0 || i != 0 && lcsLength[i][j] == lcsLength[i - 1][j]) {
            result.add(ChangedString(firstList[i - 1], Changes.DELETED))
            i--
        }

    }
    result.reverse()
    return result
}


