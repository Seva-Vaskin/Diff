import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // try to parse arguments
    val arguments : Arguments
    try {
        arguments = parseArguments(args)
    }
    catch (e: ArgumentsException) {
        if (e.message.isNotEmpty())
            System.err.println(e.message)
        System.err.println("Try 'diff --help' for more information.")
        exitProcess(1)
    }
    catch (e : FilesException) {
        if (e.message.isNotEmpty())
            System.err.println(e.message)
        exitProcess(1)
    }

    // print help message if necessary
    if (arguments.help) {
        println("Help message")
        TODO("print help message")
        return
    }

}
