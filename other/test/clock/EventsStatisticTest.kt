package clock

import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.util.*

class EventsStatisticTest {

    @Test
    fun simpleTest() {
        val clock = SetableClock()
        val stat = EventsStatistic(clock)

        for (i in 1..60) {
            clock.now = clock.now.plusSeconds(60)
            stat.incEvent("Event#$i")
        }

        stat.printStatistic()
        assert(stat.getAllEventStatistic().all { it == 1 })
        assert(stat.getEventStatisticByName("Event#13")[12] == 1)

        for (i in 1..60) {
            clock.now = clock.now.plusSeconds(60)
            for (j in 1..i) {
                stat.incEvent("Event#${i - j}")
            }
        }

        stat.printStatistic()
        val a = stat.getAllEventStatistic()
        var b = stat.getEventStatisticByName("Event#23")
        println(Arrays.toString(b))
        assert(b.contentEquals(arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)))
        for (i in 1..60) {
            clock.now = clock.now.plusSeconds(13)
            stat.incEvent("Event#23")
            assert(a[i - 1] == i)
        }

        b = stat.getEventStatisticByName("Event#23")
        println(Arrays.toString(b))
        assert(b.contentEquals(arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5, 4, 5, 5, 4, 5, 4, 5, 5, 4, 5, 5)))
        for (i in 1..60) {
            assert(a[i - 1] == i)
        }
    }
}