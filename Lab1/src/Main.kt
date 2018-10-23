fun main(args: Array<String>) {
    print("Enter cache capacity: ")
    val capacity = readLine()?.trim()?.toInt()!!
    val cache = LRUCache<String>(capacity)

    while (true) {
        try {
            val line = readLine()!!
            val command = line.split(" ")
            when (command.first()) {
                "add" -> cache.put(command[1].toInt(), command[2])

                "get" -> println(cache.get(command[1].toInt()))

                "info" -> cache.printPrivateInfo()

                else -> println("Unkhown command")
            }
        } catch (e: Throwable) {
            println("ERROR: " + e.message)
            e.printStackTrace()
        }
    }
}