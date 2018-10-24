import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.net.ssl.HttpsURLConnection

class VkTagStats {

    private val accessKey = "90d70d3b90d70d3b90d70d3b9990b1bc33990d790d70d3bcb188db854a2361307f11d14"
//    private val accessKey = "90dl0d3b90d70d3b90d70d3b9990b1bc33990d790d70d3bcb188db854a2361307f11d14"
    private val apiVersion = "5.87"
    private val baseUrl = "https://api.vk.com/method/newsfeed.search"

    private val jsonParser = JsonParser()

    fun tagHist(tag: String, hours: Int): IntArray {
        val time = Instant.now().minus(hours.toLong(), ChronoUnit.HOURS).epochSecond
        val hashTag = URLEncoder.encode("#$tag", "utf8")
        val url = "$baseUrl?q=$hashTag&start_time=$time&count=200&access_token=$accessKey&v=$apiVersion" // %23 == #
        println(url)
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"

        val response: JsonObject? = jsonParser.parse(InputStreamReader(connection.inputStream))?.asJsonObject
        println(response)
        val items: JsonArray? = response?.getAsJsonObject("response")?.getAsJsonArray("items")
        println(items)

        val hist = IntArray(hours)
        items?.forEach {
            val date: Long? = it?.asJsonObject?.get("date")?.asLong
            date?.let { d ->
                val hour: Int = Duration.between(Instant.ofEpochSecond(d), Instant.now()).toHours().toInt()
                hist[hour] += 1
            }
        }

        return hist
    }
}