package search

import akka.actor.UntypedActor
import java.net.URLEncoder
import org.jsoup.Jsoup

abstract class SearchActor(private val search: String, private val cssQuery: String) : UntypedActor() {

    override fun onReceive(query: Any?) {
        if (query is String) {
            val response = Jsoup.connect(search + URLEncoder.encode(query, "UTF-8")).userAgent("GTFO").get()
            val links = response.select(cssQuery)
            sender().tell(links.map {it.text() + " " + it.absUrl("href")}.take(5), self())
        }
    }
}

class GoogleActor : SearchActor("http://www.google.com/search?q=", ".g>.r>a")

class YandexActor : SearchActor("https://yandex.ru/search/?text=", "li h2 a")

class BingActor : SearchActor("https://www.bing.com/search?q=", "#b_results > li h2 a")

