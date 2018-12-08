package clock

import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

operator fun HashMap<String, LinkedList<Instant>>.invoke(name: String): LinkedList<Instant> {
    if (!this.containsKey(name)) {
        this[name] = LinkedList()
    }
    return this[name]!!
}

class EventsStatistic(private val clock: Clock) {
    private val log = hashMapOf<String, LinkedList<Instant>>()

    private fun update(name: String) {
        val now = clock.now
        while (log(name).isNotEmpty() && Duration.between(log(name).first, now).toMinutes() >= 60) {
            log(name).removeFirst()
        }
    }

    fun incEvent(name: String) {
        log(name).add(clock.now)
        update(name)
    }

    fun getEventStatisticByName(name: String): Array<Int> {
        update(name)
        val stat = Array(60) { 0 }
        val now = clock.now
        for (instant in log(name)) {
            stat[59 - Duration.between(instant, now).toMinutes().toInt()]++
        }

        return stat
    }

    fun getAllEventStatistic(): Array<Int> {
        val stat = Array(60) { 0 }
        val now = clock.now
        for (entry in log) {
            update(entry.key)
            for (instant in entry.value) {
                stat[59 - Duration.between(instant, now).toMinutes().toInt()]++
            }
        }

        return stat
    }

    fun printStatistic() {
        println(Arrays.toString(getAllEventStatistic()))
    }
}