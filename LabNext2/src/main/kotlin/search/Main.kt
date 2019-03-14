package search

import akka.actor.*
import scala.concurrent.duration.Duration

class MasterActor : UntypedActor() {
    private val ans: HashMap<String, List<String>> = HashMap()

    private val actors = ArrayList<ActorRef>()

    override fun onReceive(message: Any?) {
//        println(message)
        if (message is String) {
            actors.add(context.actorOf(Props.create(GoogleActor::class.java), "google"))
            actors.add(context.actorOf(Props.create(YandexActor::class.java), "yandex"))
            actors.add(context.actorOf(Props.create(BingActor::class.java), "bing"))
            context.setReceiveTimeout(Duration.fromNanos(3_000_000_000))
            actors.forEach { it.tell(message, self) }
        } else if (message is List<*>) {
            ans[sender.path().name()] = message as List<String>

            if (ans.size == 3) {
                sender.tell(ans, self())
                println("DONE!")
                stop()
            }
        } else if (message is ReceiveTimeout) {
            println("TIMEOUT!")
            stop()
        }
    }

    private fun stop() {
        for ((name, list) in ans) {
            println("$name:\n${list.joinToString(separator = "\n")}\n\n")
        }
        actors.forEach { context.stop(it) }
        context.stop(self)
    }
}

fun main(args: Array<String>) {
    val system = ActorSystem.create("PoggerSystem")
    val master = system.actorOf(Props.create(MasterActor::class.java), "main")
    master.tell("Why Google Sucks?", null)
}