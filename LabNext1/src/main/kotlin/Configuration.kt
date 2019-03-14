import clock.Clock
import clock.NormalClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy
import profiler.PerformanceAspect
import test.Shit

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
open class Configuration {
    @Bean
    open fun shit(): Shit {
        return Shit()
    }

    @Bean
    open fun clock(): Clock {
        return NormalClock()
    }

    @Bean
    open fun aspect(): PerformanceAspect {
        return PerformanceAspect(clock())
    }
}
