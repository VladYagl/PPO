import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class VkApi {
    private val accessKey = "90d70d3b90d70d3b90d70d3b9990b1bc33990d790d70d3bcb188db854a2361307f11d14"
    //    private val accessKey = "90dl0d3b90d70d3b90d70d3b9990b1bc33990d790d70d3bcb188db854a2361307f11d14"
    private val apiVersion = "5.87"
    private val baseUrl = "https://api.vk.com/method/newsfeed.search"

    private val jsonParser = JsonParser()
    private val gson = Gson()

    fun loadPosts(query: String, startTime: Long): List<Post> {
        val url = "$baseUrl?q=$query&start_time=$startTime&count=200&access_token=$accessKey&v=$apiVersion" // %23 == #
        println(url)
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"

        val response: JsonObject? = jsonParser.parse(InputStreamReader(connection.inputStream))?.asJsonObject
        println(response)
        val items: JsonArray? = response?.getAsJsonObject("response")?.getAsJsonArray("items")
        println(items)

        return items?.mapNotNull { gson.fromJson(it, Post::class.java) } ?: emptyList()
    }
}