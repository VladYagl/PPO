package search

import akka.actor.UntypedActor
import java.net.URLEncoder
import org.jsoup.Jsoup
import java.io.File

open class SearchActor(
    private val search: String,
    private val cssQuery: String,
    private val saveFile: Boolean = false
) : UntypedActor() {

    override fun onReceive(query: Any?) {
        if (query is String) {
            val response = Jsoup.connect(search + URLEncoder.encode(query, "UTF-8")).userAgent("GTFO").get()
            if (saveFile) {
                File("response_${this.self.path().name()}.html").bufferedWriter().use {
                    it.append(response.html())
                }
            }
            val links = response.select(cssQuery)
            sender().tell(links.map { it.text() + " " + it.absUrl("href") }.take(5), self())
        }
    }
}

class GoogleActor : SearchActor("http://www.google.com/search?q=", ".g>.r>a", true)

class YandexActor : SearchActor("https://yandex.ru/search/?text=", "li h2 a")

class BingActor : SearchActor("https://www.bing.com/search?q=", "#b_results > li h2 a")

