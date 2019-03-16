package search

import akka.actor.ActorSystem
import akka.actor.Props
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.*
import com.xebialabs.restito.semantics.Condition.alwaysTrue
import com.xebialabs.restito.server.StubServer
import org.junit.Assert.*
import org.junit.Test
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

private const val PORT = 32453

fun load(url: String): String {
    println(url)
    val connection = URL(url).openConnection() as HttpURLConnection

    connection.inputStream.bufferedReader().use {
        return it.readText()
    }
}

class GoodTest {

    private val response = File("response_google.html").bufferedReader().readText()

    @Test
    fun ok() {
        withStubServer(PORT) { server ->
            whenHttp(server)
                .match(alwaysTrue())
                .then(stringContent(response))

            val system = ActorSystem.create("PoggerSystem")
            val actors = listOf(
                system.actorOf(
                    Props.create(SearchActor::class.java, "http://localhost:$PORT/", ".g>.r>a", false),
                    "test"
                )
            )
            val master =
                system.actorOf(Props.create(MasterActor::class.java, actors, { ans: HashMap<String, List<String>> ->
                    assertEquals(1, ans.size)
                    assertEquals("Why Does Google Suck?", ans["test"]!![2].substringBefore(" Annoying"))
                }), "master")
            master.tell("test", null)
            Await.result(system.whenTerminated(), Duration.Inf())
        }
    }

    @Test
    fun timeout() {
        withStubServer(PORT) { server ->
            whenHttp(server)
                .match(alwaysTrue())
                .then(delay(5000), stringContent(response))

            val system = ActorSystem.create("PoggerSystem")
            val actors = listOf(
                system.actorOf(Props.create(SearchActor::class.java, "http://localhost:$PORT/", ".g>.r>a", false), "test")
            )
            val master = system.actorOf(Props.create(MasterActor::class.java, actors, { ans: HashMap<String, List<String>> ->
                assertEquals(0, ans.size)
            }), "master")
            master.tell("test", null)
            Await.result(system.whenTerminated(), Duration.Inf())
        }
    }

    private fun withStubServer(port: Int, callback: (StubServer) -> Unit) {
        var stubServer: StubServer? = null
        try {
            stubServer = StubServer(port).run()
            callback(stubServer)
        } finally {
            stubServer?.stop()
        }
    }
}