/**
 * Describes blocks of strings for printing
 */
data class PrintingBlock(
    val deleted: MutableList<String> = mutableListOf(),
    val added: MutableList<String> = mutableListOf(),
    val unchanged: MutableList<String> = mutableListOf(),
    var changes: Changes = Changes.UNCHANGED
) {
    /**
     * Prints printing block
     */
    fun print(file1Line: Int, file2Line: Int) {
        // "unchanged" block is not to print
        if (changes == Changes.UNCHANGED)
            return

        fun rangeToString(leftBorder: Int, rangeLength: Int): String =
            if (rangeLength == 1) "$leftBorder" else "$leftBorder,${leftBorder + rangeLength - 1}"

        // print change type and numbers of changed strings
        println(
            when (changes) {
                Changes.ADDED -> "${file1Line - 1}a${rangeToString(file2Line, added.size)}"
                Changes.DELETED -> "${rangeToString(file1Line, deleted.size)}d${file2Line - 1}"
                else -> "${rangeToString(file1Line, deleted.size)}c${rangeToString(file2Line, added.size)}"
            }
        )
        // prints changed strings
        deleted.forEach {
            println("< $it")
        }
        if (added.isNotEmpty() && deleted.isNotEmpty())
            println("---")
        added.forEach {
            println("> $it")
        }
    }
}

/**
 * Splits given list of ChangedString into blocks to print
 */
fun splitChangedStrings(changedStrings: List<ChangedString>): List<PrintingBlock> {
    val result = mutableListOf<PrintingBlock>()
    changedStrings.forEach {
        if (result.isEmpty()) { // make new block if empty
            result.add(
                when (it.changes) {
                    Changes.ADDED -> PrintingBlock(added = mutableListOf(it.data), changes = Changes.ADDED)
                    Changes.DELETED -> PrintingBlock(deleted = mutableListOf(it.data), changes = Changes.DELETED)
                    else -> PrintingBlock(unchanged = mutableListOf(it.data), changes = Changes.UNCHANGED)
                }
            )
        } else if (result.last().changes == it.changes) { // add string to existing block
            when (it.changes) {
                Changes.ADDED -> result.last().added.add(it.data)
                Changes.DELETED -> result.last().deleted.add(it.data)
                else -> result.last().unchanged.add(it.data)
            }
        } else if ((result.last().changes == Changes.DELETED) || (result.last().changes == Changes.CHANGED)
            && it.changes == Changes.ADDED
        ) { // (add string "add" to block "changed") or (convert block "deleted" to block "changed" and add string "add")
            result.last().changes = Changes.CHANGED
            result.last().added.add(it.data)
        } else { // make new block if we can't continue existing
            result.add(
                when (it.changes) {
                    Changes.ADDED -> PrintingBlock(added = mutableListOf(it.data), changes = Changes.ADDED)
                    Changes.DELETED -> PrintingBlock(deleted = mutableListOf(it.data), changes = Changes.DELETED)
                    else -> PrintingBlock(unchanged = mutableListOf(it.data), changes = Changes.UNCHANGED)
                }
            )
        }
    }
    return result
}

/**
 * Prints difference between files calculated into "changedStrings" using "arguments"
 * for parametrize output
 */
fun printResult(changesToPrint: List<ChangedString>, arguments: Arguments) {
    // TODO: add arguments dependence
    val printingBlocks = splitChangedStrings(changesToPrint)
    var file1Line = 1
    var file2Line = 1
    printingBlocks.forEach {
        it.print(file1Line, file2Line)
        file1Line += it.deleted.size + it.unchanged.size
        file2Line += it.added.size + it.unchanged.size
    }
}