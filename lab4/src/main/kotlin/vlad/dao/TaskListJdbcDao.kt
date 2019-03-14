package vlad.dao

import org.springframework.jdbc.core.support.JdbcDaoSupport
import vlad.model.TaskList
import javax.sql.DataSource
import org.springframework.jdbc.core.BeanPropertyRowMapper


open class TaskListJdbcDao(dataSource: DataSource) : JdbcDaoSupport(), TaskListDao {
    init {
        setDataSource(dataSource)
    }

    override fun getTaskLists(): List<TaskList> {
        val sql = "SELECT * FROM TASK_LIST"
        return jdbcTemplate?.query(sql, BeanPropertyRowMapper(TaskList::class.java)) ?: emptyList()
    }

    override fun addTaskList(list: TaskList): Int {
        val sql = "INSERT INTO TASK_LIST (NAME) VALUES (?)"
        return jdbcTemplate?.update(sql, list.name) ?: -69
    }

    override fun deleteTaskList(id: Int) {
        val sql = "DELETE FROM TASK_LIST WHERE ID = ?"
        jdbcTemplate?.update(sql, id)
        val sqlTask = "DELETE FROM TASK WHERE list = ?"
        jdbcTemplate?.update(sqlTask, id)
    }
}
