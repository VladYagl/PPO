package main

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class TagStats(private val api: VkApi) {

    fun tagHist(tag: String, hours: Int): IntArray {
        val time = Instant.now().minus(hours.toLong(), ChronoUnit.HOURS).epochSecond
        val posts = api.loadPosts("#$tag", time)
        println("post count : " + posts.size)

        val hist = (0 until hours).associate { Pair(it, 0) }.toMutableMap()
        return posts.groupingBy {
            Duration.between(Instant.ofEpochSecond(it.date), Instant.now()).toHours().toInt()
        }.eachCountTo(hist).values.reversed().toIntArray()
    }
}
