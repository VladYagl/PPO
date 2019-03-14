package test

import main.Post
import org.junit.jupiter.api.Test
import java.io.File

import main.parse
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals

private const val sample = "res/sample_response.json"

class PostParserTest {

    private val response: String = File(sample).inputStream().bufferedReader().use {
        it.readText()
    }

    @Test
    fun parseSample() {
        val posts = parse(response)
        assertEquals(posts, listOf(
                Post(26163, 1540400485, "text1 #knife"),
                Post(3246, 1540399980, "text2 #knife"),
                Post(138334, 1540398446, "text3 #knife")
        ))
    }

    @Test
    fun parseError() {
        val posts = parse("{error: \"Deep cut\"}")
        assert(posts.isEmpty())

        assertThrows<IllegalStateException> {
            parse("SHIT")
        }
        assertThrows<IllegalStateException> {
            parse("")
        }
    }
}
