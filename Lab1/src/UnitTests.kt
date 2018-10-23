import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class LRUCacheTest {
    private val cacheTiny = LRUCache<String>(3)
    private val cacheSmall = LRUCache<String>(10)
    private val cacheHuge = LRUCache<String>(2000)

    private val cacheList = listOf(cacheTiny, cacheSmall, cacheHuge)

    @Test
    fun simpleTest() {
        val cache = cacheTiny

        cache.put(1, "first")
        cache.put(2, "second")
        cache.put(3, "third")
        Assertions.assertEquals("second", cache.get(2))
        Assertions.assertEquals("first", cache.get(1))
        cache.put(4, "forth")
        Assertions.assertEquals("second", cache.get(2))
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(3) }
        cache.put(5, "fifth")
        Assertions.assertEquals("second", cache.get(2))
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(1) }
        cache.put(6, "sixth")
        Assertions.assertEquals("fifth", cache.get(5))
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(4) }
        cache.put(7, "wrong text")
        cache.put(7, "seventh")
        Assertions.assertEquals("fifth", cache.get(5))
        Assertions.assertEquals("seventh", cache.get(7))
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(2) }
        cache.put(-1, "stub")
        cache.put(-2, "stub")
        Assertions.assertEquals("seventh", cache.get(7))
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(5) }
        Assertions.assertThrows(NoSuchElementException::class.java) { cache.get(6) }
    }

    @Test
    fun onlyAdding() {
        for (cache in cacheList) {
            (1..cache.capacity).forEach {
                cache.put(it, "Entry number $it")
            }

            (1..10).forEach {
                cache.put(-it, "Stub $it")
                Assertions.assertThrows(NoSuchElementException::class.java) {
                    cache.get(it)
                }
            }
        }
    }

    private val rand = Random(1488)

    private fun Random.nextString(): String {
        return (1..this.nextInt(100)).joinToString { this.nextInt().toChar().toString() }
    }

    @Test
    fun randomTest() {
        for (cache in cacheList) {
            val list = LinkedList<Pair<Int, String>>()
            println("Random test on cache capacity: " + cache.capacity)

            (1..cache.capacity * 10).forEach { _ ->
                val key = rand.nextInt()
                val value = rand.nextString()

                cache.put(key, value)
                list.add(Pair(key, value))
                if (list.size > cache.capacity) {
                    Assertions.assertThrows(NoSuchElementException::class.java) {
                        cache.get(list.first.first)
                    }
                    list.removeFirst()
                }

                (1..rand.nextInt(20)).forEach { _ ->
                    val pair = list[rand.nextInt(list.size)]
                    Assertions.assertEquals(pair.second, cache.get(pair.first))
                    list.remove(pair)
                    list.add(pair)
                }
            }
        }
    }

    @Test
    fun wrongSize() {
        (-10..0).forEach {
            Assertions.assertThrows(Exception::class.java) {
                LRUCache<Int>(it)
            }
        }
    }
}