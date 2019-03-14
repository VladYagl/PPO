package main

import java.util.*

fun main(args: Array<String>) {
    println(Arrays.toString(TagStats(VkApi()).tagHist("knife", 23)))
}