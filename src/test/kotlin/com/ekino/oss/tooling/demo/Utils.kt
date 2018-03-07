package com.ekino.oss.tooling.demo

import io.kotlintest.matchers.Matcher
import io.kotlintest.matchers.match
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.contrib.java.lang.system.SystemErrRule
import org.junit.contrib.java.lang.system.SystemOutRule
import org.junit.runners.model.Statement
import java.io.File

object Utils {

    fun loadResource(path: String) = Utils::class.java.getResource(path)!!
}

fun String.resourcePath() = Utils.loadResource(this).path!!

fun String.resourceFile() = File(resourcePath())

fun runCommandLine(vararg args: String, expectedExitCode: Int = 0, logAssertions: (outLog: String, errLog: String) -> Unit = { _: String, _: String -> }) {
    checkExitAndExtractLog({
        main(arrayOf(*args))
    }, expectedExitCode, logAssertions)
}

fun checkExitAndExtractLog(workToDo: () -> Unit, expectedExitCode: Int, logAssertions: (outLog: String, errLog: String) -> Unit) {
    val statement: Statement = object : Statement() {
        override fun evaluate() {
            workToDo()
        }
    }
    val systemExit = ExpectedSystemExit.none()
    systemExit.expectSystemExitWithStatus(expectedExitCode)
    val systemOutRule = SystemOutRule()
    systemOutRule.enableLog()
    val systemErrRule = SystemErrRule()
    systemErrRule.enableLog()
    systemExit.checkAssertionAfterwards({
        logAssertions(systemOutRule.log, systemErrRule.log)
    })
    systemExit.apply(systemOutRule.apply(systemErrRule.apply(statement, null), null), null).evaluate()
    systemErrRule.clearLog()
    systemOutRule.clearLog()
}

fun empty(): Matcher<String> = match("")
