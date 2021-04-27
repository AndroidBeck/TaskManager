package ru.aevd.taskmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.aevd.taskmanager.databinding.ActivityMainBinding
import ru.aevd.taskmanager.domain.entities.Task
import ru.aevd.taskmanager.domain.loadTasks
import ru.aevd.taskmanager.ui.adapters.TasksListAdapter
import java.sql.Timestamp
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tasks: List<Task> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set calendar
        val today = Calendar.getInstance()
        binding.datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        ) {
            view, year, month, day ->
            val month = month + 1
            //Filter tasks according to the picked date
            val startDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 00:00:00").time / 1000
            val endDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 23:59:59").time / 1000
            Log.d("MainActivity", "Timestamp: $day $month $year $startDayTimestamp $endDayTimestamp")
            val filteredTasks = tasks.filter { task ->
                task.dateStart < endDayTimestamp && task.dateFinish > startDayTimestamp
            }
            //Update adapter when pick a date
            updateAdapter(filteredTasks)
            binding.tasksRecycler.isVisible = filteredTasks.isNotEmpty()
            binding.noTasksTextView.isVisible = filteredTasks.isEmpty()
        }

        //Set recycler
        tasks = fakeTasksList()
        binding.tasksRecycler.adapter = TasksListAdapter()
        binding.tasksRecycler.layoutManager = LinearLayoutManager(
            baseContext, LinearLayoutManager.VERTICAL, false)


        //Parse Json
        CoroutineScope(Dispatchers.IO).launch {
            tasks = loadTasks(applicationContext)
        }

    }

    private fun updateAdapter(tasks: List<Task>) {
        (binding.tasksRecycler.adapter as TasksListAdapter).bindTasks(tasks)
    }
}

fun fakeTasksList(): List<Task> = listOf(
    Task(1, "Task1", "Description1", 1619478000, 1619481599),
    Task(2, "Task2", "Description2", 1619510400, 1619524800)
)