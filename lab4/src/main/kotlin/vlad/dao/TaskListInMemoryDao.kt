package vlad.dao

import vlad.model.TaskList
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

class TaskListInMemoryDao : TaskListDao {
    private val lastId = AtomicInteger(0)
    private val tasks = CopyOnWriteArrayList<TaskList>()

    override fun getTaskLists(): List<TaskList> {
        return ArrayList(tasks)
    }

    override fun addTaskList(list: TaskList): Int {
        val id = lastId.incrementAndGet()
        list.id = id
        tasks.add(list)
        return id
    }

    override fun deleteTaskList(id: Int) {
        tasks.removeIf { it.id == id }
    }
}