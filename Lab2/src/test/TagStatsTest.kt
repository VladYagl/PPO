package test

import main.Post
import main.TagStats
import main.VkApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import org.mockito.Mockito
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

internal class TagStatsTest {

    @Mock
    private lateinit var api: VkApi

    private lateinit var stats: TagStats

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        stats = TagStats(api)
    }

    @Test
    fun tagHist() {
        val hours = 5
        val time = Instant.now().minus(hours.toLong(), ChronoUnit.HOURS).epochSecond

        val time1 = Instant.now().minus(1, ChronoUnit.HOURS).epochSecond
        val time2 = Instant.now().minus(2, ChronoUnit.HOURS).epochSecond
        val time3 = Instant.now().minus(4, ChronoUnit.HOURS).epochSecond

        Mockito.`when`(api.loadPosts("#knife", time))
                .thenReturn(listOf(
                        Post(26163, time1, "text1 #knife"),
                        Post(3246, time2, "text2 #knife"),
                        Post(138334, time3, "text3 #knife")
                ))

        assert(Arrays.equals(stats.tagHist("knife", hours), intArrayOf(1, 0, 1, 1, 0)))
    }
}