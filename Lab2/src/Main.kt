import java.util.*

fun main(args: Array<String>) {
    val shit = VkTagStats()
    println(Arrays.toString(shit.tagHist("пост", 10)))
}