package vlad.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import vlad.dao.TaskDao
import vlad.dao.TaskInMemoryDao
import vlad.dao.TaskListDao
import vlad.dao.TaskListInMemoryDao


@Configuration
open class InMemoryDaoContextConfiguration {
    @Bean
    open fun taskDao(): TaskDao {
        return TaskInMemoryDao()
    }

    @Bean
    open fun taskListDao(): TaskListDao {
        return TaskListInMemoryDao()
    }
}