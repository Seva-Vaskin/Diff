fun printResult(changedStrings: List<ChangedString>, arguments: Arguments) {
    changedStrings.forEach {
        print(
            when (it.changes) {
                Changes.ADDED -> "+ "
                Changes.DELETED -> "- "
                else -> "* "
            }
        )
        println(it.data)
    }
}