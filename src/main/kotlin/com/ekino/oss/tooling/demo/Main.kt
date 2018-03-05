package com.ekino.oss.tooling.demo

import java.io.OutputStreamWriter
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    displayAndExit("Hello world !")
}

fun displayAndExit(message: String = "", exitCode: Int = 0) {
    val writer = OutputStreamWriter(if (exitCode == 0) System.out else System.err)
    writer.write("$message\n")
    writer.flush()
    exitProcess(exitCode)
}
