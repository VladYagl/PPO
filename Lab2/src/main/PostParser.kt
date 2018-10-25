package main

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

private val jsonParser = JsonParser()
private val gson = Gson()

fun parse(response: String): List<Post> {
    val json: JsonObject? = jsonParser.parse(response)?.asJsonObject
    val items: JsonArray? = json?.getAsJsonObject("response")?.getAsJsonArray("items")

    return items?.mapNotNull { gson.fromJson(it, Post::class.java) } ?: run {
        println(response)
        return emptyList()
    }
}