package test

import org.junit.jupiter.api.Test

import com.xebialabs.restito.server.StubServer
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.*
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException
import kotlin.test.assertEquals


private const val PORT = 32453

internal class UrlUtilsTest {

    @Test
    fun load() {
        withStubServer(PORT) { server ->
            whenHttp(server)
                    .match(startsWithUri("/ping"))
                    .then(stringContent("pong"))

            val response = main.load("https://localhost:$PORT/ping")

            assertEquals("pong", response)
        }
    }

    @Test
    fun loadError() {
        assertThrows<FileNotFoundException> {
            withStubServer(PORT) { server ->
                whenHttp(server)
                        .match(startsWithUri("/ping"))
                        .then(status(HttpStatus.NOT_FOUND_404))

                main.load("https://localhost:$PORT/ping")
            }
        }
    }

    private fun withStubServer(port: Int, callback: (StubServer) -> Unit) {
        var stubServer: StubServer? = null
        try {
            stubServer = StubServer(port).secured().run()
            callback(stubServer)
        } finally {
            stubServer?.stop()
        }
    }
}