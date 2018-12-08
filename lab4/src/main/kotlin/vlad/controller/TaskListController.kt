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
class TaskListController {
    @Autowired
    private val taskListDao: TaskListDao? = null

    @RequestMapping(value = ["/delete-list"], method = [RequestMethod.GET])
    fun deleteList(@RequestParam("id") id: Int): String {
        taskListDao!!.deleteTaskList(id)
        return "redirect:/"
    }

    @RequestMapping(value = ["/add-list"], method = [RequestMethod.POST])
    fun addList(@ModelAttribute("list") list: TaskList): String {
        taskListDao!!.addTaskList(list)
        return "redirect:/"
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun getTasks(map: ModelMap): String {
        prepareModelMap(map, taskListDao!!.getTaskLists())
        return "list"
    }

    private fun prepareModelMap(map: ModelMap, lists: List<TaskList>) {
        map.addAttribute("lists", lists)
        map.addAttribute("newList", TaskList())
    }
}