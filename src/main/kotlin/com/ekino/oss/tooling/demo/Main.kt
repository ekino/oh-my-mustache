package com.ekino.oss.tooling.demo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.mustachejava.DefaultMustacheFactory
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import com.xenomachina.common.orElse
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.StringReader
import java.io.Writer
import kotlin.system.exitProcess

fun main(args: Array<String>) = mainBody {

    val parsedArgs = ArgParser(args).parseInto(::MainArgs)

    if (parsedArgs.verbose) {
        println("Verbose mode enabled")
        println("Input parameters : ${args.toList()}\n")
    }

    if (parsedArgs.version) {
        exit("${Application.name} version ${Application.config[project.version]}")
    }

    val context = mutableMapOf<String, Any>()
    parsedArgs.contextFile
            ?.let { jacksonObjectMapper().readValue<Map<String, Any>>(it) }
            ?.let { context.putAll(it) }

    parsedArgs.simpleContext
            .let { context.putAll(it) }

    val templateReader = parsedArgs.templateFile
            ?.let { FileReader(it) }
            ?: parsedArgs.templateText
                    ?.let { StringReader(it) }
            ?: throw InvalidArgumentException("No template input")

    transformAndPrint(templateReader, context, OutputStreamWriter(System.out))
    exit()
}

fun transformAndPrint(template: Reader, context: Map<String, Any>, writer: Writer) {
    val mf = DefaultMustacheFactory()
    val mustache = mf.compile(template, "template")
    mustache.execute(writer, context)
    writer.flush()
}

class MainArgs(parser: ArgParser) {

    val verbose by parser.flagging(
            "--verbose",
            help = "enable the verbose mode"
    )

    val version by parser.flagging(
            "--version",
            help = "display the version and exit"
    )

    val contextFile by parser.storing(
            "--context-file",
            help = "context file path") {
        File(this)
    }
            .default<File?>(null)
            .addValidator {
                value?.let {
                    if (!it.exists()) throw InvalidArgumentException("Unable to find file at path ${it.path}")
                }
            }

    val simpleContext by parser.option<MutableMap<String, String>>(
            "-v", "--variable",
            argNames = listOf("NAME", "VALUE"),
            isRepeating = true,
            help = "simple variable to add to context") {
        value.orElse { mutableMapOf<String, String>() }.apply { put(arguments[0], arguments[1]) }
    }
            .default(mutableMapOf<String, String>())

    val templateFile by parser.storing(
            "--template-file",
            help = "template file path") {
        File(this)
    }
            .default<File?>(null)
            .addValidator {
                value?.let {
                    if (!it.exists()) throw InvalidArgumentException("Unable to find file at path ${it.path}")
                }
            }

    val templateText by parser.storing(
            "-t", "--template",
            help = "template text")
            .default<String?>(null)
}

fun exit(message: String = "", exitCode: Int = 0) {
    val writer = OutputStreamWriter(if (exitCode == 0) System.out else System.err)
    writer.write("$message\n")
    writer.flush()
    exitProcess(exitCode)
}
