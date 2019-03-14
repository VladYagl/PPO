package profiler

import clock.SetableClock
import org.junit.Before
import org.junit.Test
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import kotlin.test.assertEquals

open class A {
    open fun simple() {
        clock.add(30)
    }

    open fun recursive(depth: Int = 0) {
        proxy.simple()
        proxy2.call_other()
        clock.add(1)
        if (depth < 10)
            proxy.recursive(depth + 1)
    }

    open fun forloop() {
        for (i in (0..100)) {
            proxy.recursive()
            for (i in (0..10)) {
                proxy.simple()
                proxy2.call_other()
            }
        }
    }
}

open class B {
    open fun call_other() {
        proxy.simple()
        proxy.simple()
    }
}

//val ctx = AnnotationConfigApplicationContext(TestConfiguration::class.java)
val clock = SetableClock()
var proxy: A = A()
var proxy2: B = B()
var aspect: PerformanceAspect = PerformanceAspect(clock)

class PerformanceAspectTest {

    @Before
    fun before() {
        aspect = PerformanceAspect(clock)

        val target = A()
        val factory = AspectJProxyFactory(target)
        factory.addAspect(aspect)
        proxy = factory.getProxy<A>()

        val target2 = B()
        val factory2 = AspectJProxyFactory(target2)
        factory2.addAspect(aspect)
        proxy2 = factory2.getProxy<B>()

        println()
    }

    @Test
    fun simpleTest() {
        proxy.simple()

        println(aspect.current.print())
        assertEquals(30_000_000_000, aspect.current.time)
    }

    @Test
    fun otherTest() {
        proxy2.call_other()

        println(aspect.current.print())
        assertEquals(60_000_000_000, aspect.current.time)
    }

    @Test
    fun recursiveTest() {
        proxy2.call_other()
        proxy.recursive()

        println(aspect.current.print())
        assertEquals(1061_000_000_000, aspect.current.time)
    }

    @Test
    fun loopTest() {
        proxy.forloop()

        println(aspect.current.print())
        assertEquals(201091_000_000_000, aspect.current.time)
    }
}
