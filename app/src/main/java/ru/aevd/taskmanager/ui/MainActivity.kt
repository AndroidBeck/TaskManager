package ru.aevd.taskmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ru.aevd.taskmanager.databinding.ActivityMainBinding
import ru.aevd.taskmanager.domain.entities.Task
import ru.aevd.taskmanager.ui.adapters.TasksListAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tasks: List<Task> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set recycler
        tasks = fakeTasksList()
        binding.tasksRecycler.adapter = TasksListAdapter()
        binding.tasksRecycler.layoutManager = LinearLayoutManager(
            baseContext, LinearLayoutManager.VERTICAL, false)
        updateAdapter(tasks)

        //Set calendar
        val today = Calendar.getInstance()
        binding.datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        ) {
            view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAdapter(tasks: List<Task>) {
        (binding.tasksRecycler.adapter as TasksListAdapter).bindTasks(tasks)
    }
}

fun fakeTasksList(): List<Task> = listOf(
    Task("Task1", "Description1"),
    Task("Task2", "Description2")
)