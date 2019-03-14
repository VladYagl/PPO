package profiler

import clock.Clock
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration

class Node(val name: String, val clazz: String) {
    val children = HashMap<String, Node>()
    var time = 0L
    var count = 0

    fun print(depth: Int = 0): String {
        if (depth == 0) {
            time = children.values.fold(0L) {acc, node -> acc + node.time }
        }
        val seconds = time.toDouble() / 1000_000_000
        return "|\t".repeat(depth) + "> $clazz.$name ${seconds}s - $count times (Avg: ${seconds / count}s)" +
                if (children.isNotEmpty()) children.values.sortedBy { -it.time }.joinToString(prefix = "\n", separator = "\n") { it.print(depth + 1) } else ""
    }
}

@Aspect
class PerformanceAspect(val clock: Clock) {
//    @Autowired
//    val clock: Clock? = null

    var calls = 0
    var current = Node("root", "root")

    @Around("within(test.*) || within(profiler.*)")
//    @Around("execution(* *(..))")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = clock.now
        val name = joinPoint.signature.name

        return if (name != current.name) {
            val old = current
            val child = if (current.children.containsKey(name)) current.children[name]!! else {
                val newborn = Node(name, joinPoint.signature.declaringTypeName)
                current.children[name] = newborn
                newborn
            }
            current = child

            val result = joinPoint.proceed(joinPoint.args)
            val end = clock.now
            val time = (Duration.between(start, end).toNanos())

            current = old
            child.time += time
            child.count++

            result
        } else {
            joinPoint.proceed(joinPoint.args)
        }
    }
}
