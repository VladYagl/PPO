package vlad.config

import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@Configuration
@EnableWebMvc
@ComponentScan("vlad.controller")
@Import(JdbcDaoContextConfiguration::class)
open class WebConfig : WebMvcConfigurerAdapter() {

    @Bean
    open fun configureInternalResourceViewResolver(): InternalResourceViewResolver {
        val resolver = InternalResourceViewResolver()
        resolver.setPrefix("WEB-INF/views/")
        resolver.setSuffix(".jsp")
        return resolver
    }
}

