import java.net.URLEncoder
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class VkTagStats {

    private val api = VkApi()

    fun tagHist(tag: String, hours: Int): IntArray {
        val time = Instant.now().minus(hours.toLong(), ChronoUnit.HOURS).epochSecond
        val hashTag = URLEncoder.encode("#$tag", "utf8")

        val posts = api.loadPosts(hashTag, time)

        val hist = IntArray(hours)
        posts.forEach {
            val hour: Int = Duration.between(Instant.ofEpochSecond(it.date), Instant.now()).toHours().toInt()
            hist[hour] += 1
        }

        return hist
    }
}