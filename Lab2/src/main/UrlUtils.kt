package main

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun load(url: String): String {
    println(url)
    val connection = URL(url).openConnection() as HttpsURLConnection

    with(connection.inputStream.bufferedReader()) {
        return readText()
    }
}
