package com.ekino.oss.tooling.demo

import com.github.mustachejava.DefaultMustacheFactory
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.StringReader
import java.io.Writer
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val templateReader = StringReader("Hello {{name}} !")
    val context = mapOf("name" to "world")
    transformAndPrint(templateReader, context, OutputStreamWriter(System.out))
    exit()
}

fun transformAndPrint(template: Reader, context: Map<String, Any>, writer: Writer) {
    val mf = DefaultMustacheFactory()
    val mustache = mf.compile(template, "template")
    mustache.execute(writer, context)
    writer.flush()
}

fun exit(message: String = "", exitCode: Int = 0) {
    val writer = OutputStreamWriter(if (exitCode == 0) System.out else System.err)
    writer.write("$message\n")
    writer.flush()
    exitProcess(exitCode)
}
