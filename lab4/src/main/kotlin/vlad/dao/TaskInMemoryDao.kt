package vlad.dao

import vlad.model.Task
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

open class TaskInMemoryDao : TaskDao {
    private val lastId = AtomicInteger(0)
    private val tasks = CopyOnWriteArrayList<Task>()

    override fun getTasks(): List<Task> {
        return ArrayList(tasks)
    }

    override fun addTask(task: Task): Int {
        val id = lastId.incrementAndGet()
        task.id = id
        tasks.add(task)
        return id
    }

    override fun checkTask(id: Int) {
        val task = tasks.find { it.id == id }!!
        task.completed = !task.completed
    }
}