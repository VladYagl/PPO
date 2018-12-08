package vlad.dao

import vlad.model.Task
import java.util.*

interface TaskDao {
    fun addTask(task: Task): Int

    fun getTasks(): List<Task>

    fun checkTask(id: Int)
}
