package vlad.controller

import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import vlad.dao.TaskDao
import vlad.dao.TaskListDao
import vlad.model.Task
import vlad.model.TaskList


@Controller
open class TaskController {
    @Autowired
    private val taskDao: TaskDao? = null

    @Autowired
    private val taskListDao: TaskListDao? = null

    @RequestMapping(value = ["/check-task"], method = [RequestMethod.GET])
    fun checkTask(@RequestParam("id") id: Int): String {
        taskDao!!.checkTask(id)
        return "redirect:/get-tasks?list=${taskDao.getTasks().first { it.id == id }.list}"
    }

    @RequestMapping(value = ["/add-task"], method = [RequestMethod.POST])
    fun addTask(@ModelAttribute("task") task: Task, @RequestParam("list_id") listId: Int): String {
        task.list = listId
        taskDao!!.addTask(task)
        return "redirect:/get-tasks?list=${task.list}"
    }

    @RequestMapping(value = ["/get-tasks"], method = [RequestMethod.GET])
    fun getTasks(@RequestParam("list") listId: Int, map: ModelMap): String {
        prepareModelMap(
            map,
            taskDao!!.getTasks().filter { it.list == listId },
            taskListDao!!.getTaskLists().first { it.id == listId }
        )
        return "index"
    }

    private fun prepareModelMap(map: ModelMap, tasks: List<Task>, taskList: TaskList) {
        map.addAttribute("tasks", tasks)
        map.addAttribute("newTask", Task())
        map.addAttribute("list", taskList)
    }
}