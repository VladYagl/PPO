package search

import akka.actor.*
import scala.concurrent.duration.Duration

class MasterActor(private val actors: List<ActorRef>, private val callback: (HashMap<String, List<String>>) -> Unit) :
    UntypedActor() {
    private val ans: HashMap<String, List<String>> = HashMap()

    override fun onReceive(message: Any?) {
        if (message is String) {
            println("Query: '$message'")
            context.setReceiveTimeout(Duration.fromNanos(3_000_000_000))
            actors.forEach { it.tell(message, self) }
        } else if (message is List<*>) {
            ans[sender.path().name()] = message as List<String>

            if (ans.size == actors.size) {
                sender.tell(ans, self())
                println("DONE!\n")
                stop()
            }
        } else if (message is ReceiveTimeout) {
            println("TIMEOUT!\n")
            stop()
        }
    }

    private fun stop() {
        actors.forEach { context.stop(it) }
        callback(ans)
        context.stop(self)
        context.system().terminate()
    }
}

fun main(args: Array<String>) {
    val system = ActorSystem.create("PoggerSystem")

    val actors = listOf(
        system.actorOf(Props.create(GoogleActor::class.java), "google"),
        system.actorOf(Props.create(YandexActor::class.java), "yandex"),
        system.actorOf(Props.create(BingActor::class.java), "bing")
    )

    val master = system.actorOf(Props.create(MasterActor::class.java, actors, { ans: HashMap<String, List<String>> ->
        for ((name, list) in ans) {
            println("$name:\n${list.joinToString(separator = "\n")}\n\n")
        }
    }), "master")
    master.tell("Why Google Sucks?", null)

}