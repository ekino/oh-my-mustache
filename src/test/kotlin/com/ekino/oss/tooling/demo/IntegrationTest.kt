package com.ekino.oss.tooling.demo

import io.kotlintest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests on command line output")
class IntegrationTest {

    @Test
    @DisplayName("Should display hello world")
    fun should_display_hello_world() {

        runCommandLine { out: String, err: String ->
            out shouldBe """
Hello world !
""".trimStart()
            err shouldBe empty()
        }
    }
}
