import java.io.File

/**
 * This class describes arguments object
 */
data class Arguments(var firstFileName: String = "",
                     var secondFileName: String = "",
                     var help: Boolean = false)


/**
 * This class is exception class for describe exceptions connected with arguments
 */
class ArgumentsException(override val message: String = "") : Exception(message)

/**
 * This class is exception class for describe exceptions connected with files and file names
 */
class FilesException(override val message: String = "") : Exception(message)

/**
 * Parses arguments 'args' and returns Arguments object
 * or finish program with error message if:
 * 1) there is no 1st or 2nd file name in arguments
 * 2) file is not exists
 * 3) file is unreadable
 * 4) there is extra operand in arguments
 */
fun parseArguments(args: Array<String>): Arguments {
    val arguments = Arguments()
    for (argument in args) {
        when (argument) {
            "--help" -> arguments.help = true
            else -> {
                if (arguments.firstFileName.isEmpty())
                    arguments.firstFileName = argument
                else if (arguments.secondFileName.isEmpty())
                    arguments.secondFileName = argument
                else {
                    throw ArgumentsException("Extra operand '$argument'")
                }
            }
        }
    }
    // if 'help' flag is in args it doesn't matter which other arguments we have
    if (arguments.help) {
        return arguments
    }
    // check that both file names was in args
    if (arguments.firstFileName.isEmpty() || arguments.secondFileName.isEmpty()) {
            throw ArgumentsException()
    }
    /**
     * This function checks that file name is correct.
     * If file names is not correct throws FilesException
     */
    fun checkFileName(fileName: String) {
        val file = File(fileName)
        if (!file.isFile) {
            throw FilesException("$fileName: No such file or directory")
        }
        if (!file.canRead()) {
            throw FilesException("$fileName: Permission denied")
        }
    }
    // check that both file names are correct paths to readable files
    checkFileName(arguments.firstFileName)
    checkFileName(arguments.secondFileName)
    return arguments
}
