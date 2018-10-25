package main

import java.net.URLEncoder

private const val accessKey = "90d70d3b90d70d3b90d70d3b9990b1bc33990d790d70d3bcb188db854a2361307f11d14"
private const val apiVersion = "5.87"
private const val baseUrl = "https://api.vk.com/method/newsfeed.search"

class VkApi {
    fun loadPosts(query: String, startTime: Long): List<Post> {
        return parse(load(buildUrl(query, startTime)))
    }

    private fun buildUrl(query: String, startTime: Long): String {
        val q = URLEncoder.encode(query, "utf8")
        return "$baseUrl?q=$q&start_time=$startTime&count=200&access_token=$accessKey&v=$apiVersion"
    }
}
