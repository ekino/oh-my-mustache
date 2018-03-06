package com.ekino.oss.tooling.demo

import io.kotlintest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests on command line output")
class IntegrationTest {

    @Test
    @DisplayName("should display help")
    fun should_display_help() {

        runCommandLine("--help") { out: String, err: String ->
            out shouldBe """
usage: [-h] [--verbose] [--version] [--context-file CONTEXT_FILE]
       [-v NAME VALUE]... [--template-file TEMPLATE_FILE] [-t TEMPLATE]

optional arguments:
  -h, --help                      show this help message and exit

  --verbose                       enable the verbose mode

  --version                       display the version and exit

  --context-file CONTEXT_FILE     context file path

  -v NAME VALUE,                  simple variable to add to context
  --variable NAME VALUE

  --template-file TEMPLATE_FILE   template file path

  -t TEMPLATE,                    template text
  --template TEMPLATE

""".trimStart()
            err shouldBe empty()
        }
    }

    @Test
    @DisplayName("should display version")
    fun should_display_version() {

        runCommandLine("--version") { out: String, err: String ->
            out shouldBe """
oh-my-mustache version SOME_TEST_VERSION
"""
                    .trimStart()
            err shouldBe empty()
        }
    }

    @Test
    fun should_display_verbose_mode() {

        runCommandLine("--version", "--verbose") { out: String, err: String ->
            out shouldBe """
Verbose mode enabled
Input parameters : [--version, --verbose]

oh-my-mustache version SOME_TEST_VERSION
"""
                    .trimStart()
            err shouldBe empty()
        }
    }

    @Test
    @DisplayName("no template input should be in error")
    fun no_template_input_should_be_in_error() {

        runCommandLine(expectedExitCode = 2) { out: String, err: String ->
            out shouldBe empty()
            err shouldBe """
No template input
"""
                    .trimStart()
        }
    }

    @Test
    @DisplayName("should use template from text arg and context from simple variables")
    fun should_use_template_from_text_arg_and_context_from_simple_variables() {

        runCommandLine(
                "--template", "Hi {{name}} !",
                "--variable", "name", "John"
        ) { out: String, err: String ->
            out shouldBe """
Hi John !
""".trimStart()
            err shouldBe empty()
        }
    }

    @Test
    @DisplayName("should use template from mustache file and context from json file")
    fun should_use_template_from_mustache_file_and_context_from_json_file() {

        runCommandLine(
                "--template-file", "/template.mustache".resourcePath(),
                "--context-file", "/context.json".resourcePath()
        ) { out: String, err: String ->
            out shouldBe """
Hi John Doe !
You have these events today :
    - Meeting 1
    - Meeting 2

""".trimStart()
            err shouldBe empty()
        }
    }

    @Test
    @DisplayName("should use context from json file and override with variable")
    fun should_use_context_from_json_file_and_override_with_variable() {

        runCommandLine(
                "--template-file", "/template.mustache".resourcePath(),
                "--context-file", "/context.json".resourcePath(),
                "--variable", "contact.lastname", "Wayne"
        ) { out: String, err: String ->
            out shouldBe """
Hi John Wayne !
You have these events today :
    - Meeting 1
    - Meeting 2

""".trimStart()
            err shouldBe empty()
        }
    }
}
