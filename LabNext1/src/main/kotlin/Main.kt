
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import profiler.PerformanceAspect
import test.Shit

val ctx = AnnotationConfigApplicationContext(Configuration::class.java)

fun main(args: Array<String>) {

    val shit = ctx.getBean(Shit::class.java)
    shit.takeLong()

    println("Result:")
    println(ctx.getBean(PerformanceAspect::class.java).current.print())
}
