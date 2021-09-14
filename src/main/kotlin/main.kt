import java.io.File
import kotlin.system.exitProcess

/**
 * check if file is correct for diff utility
 */
fun checkFile(file: File): Boolean {
    if (!file.isFile) {
        System.err.println("${file.name}: No such file or directory")
        return false
    }
    if (!file.canRead()) {
        System.err.println("${file.name}: Can't read this file")
        return false
    }
    return true
}

fun main(args: Array<String>) {
    if ("--help" in args) {
        println("Help message")
        TODO("print help message here")
    }
    if (args.size < 2) {
        System.err.println("Try 'diff --help' for more information.")
        exitProcess(1)
    }
    val firstFileName: String = args[0]
    val secondFileName: String = args[0]
    val firstFile = File(firstFileName)
    val secondFile = File(secondFileName)
    if (!checkFile(firstFile) || !checkFile(secondFile)) {
        exitProcess(1)
    }
}
