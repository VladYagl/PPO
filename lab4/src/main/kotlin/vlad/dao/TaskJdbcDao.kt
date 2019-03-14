package vlad.dao

import org.springframework.jdbc.core.support.JdbcDaoSupport
import vlad.model.Task
import javax.sql.DataSource
import org.springframework.jdbc.core.BeanPropertyRowMapper


open class TaskJdbcDao(dataSource: DataSource) : JdbcDaoSupport(), TaskDao {
    init {
        setDataSource(dataSource)
    }

    override fun getTasks(): List<Task> {
        val sql = "SELECT * FROM TASK"
        return jdbcTemplate?.query(sql, BeanPropertyRowMapper(Task::class.java)) ?: emptyList()
    }

    override fun addTask(task: Task): Int {
        val sql = "INSERT INTO TASK (NAME, COMPLETED, LIST) VALUES (?, ?, ?)"
        return jdbcTemplate?.update(sql, task.name, task.completed, task.list) ?: -69
    }

    override fun checkTask(id: Int) {
        val sql = "UPDATE TASK SET completed = not completed WHERE id = ?"
        jdbcTemplate?.update(sql, id)
    }
}
