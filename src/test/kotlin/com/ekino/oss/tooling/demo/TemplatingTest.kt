package com.ekino.oss.tooling.demo

import io.kotlintest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.FileReader
import java.io.StringReader
import java.io.StringWriter

@DisplayName("Tests about mustache templating")
class TemplatingTest {

    @Test
    @DisplayName("Should convert simple template from String")
    fun should_convert_simple_template_from_string() {

        // Given
        val template = """
                Hi {{name}} !
            """.trimIndent()
        val reader = StringReader(template)
        val context = mapOf("name" to "John")

        // When
        val writer = StringWriter()
        transformAndPrint(reader, context, writer)

        // Then
        writer.toString() shouldBe "Hi John !"
    }

    @Test
    @DisplayName("Should convert template from File")
    fun should_convert_template_from_file() {

        // Given
        val reader = FileReader("/template.mustache".resourceFile())
        val context = mapOf(
                "contact" to mapOf(
                        "firstname" to "John",
                        "lastname" to "Doe"
                ),
                "events" to listOf(
                        mapOf("title" to "Meeting 1"),
                        mapOf("title" to "Meeting 2")
                )
        )

        // When
        val writer = StringWriter()
        transformAndPrint(reader, context, writer)

        // Then
        writer.toString() shouldBe """
Hi John Doe !
You have these events today :
    - Meeting 1
    - Meeting 2
""".trimStart()
    }
}
