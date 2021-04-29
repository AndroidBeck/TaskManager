package ru.aevd.taskmanager.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aevd.taskmanager.domain.entities.Task
import ru.aevd.taskmanager.domain.loadTasksFromJSon
import java.sql.Timestamp
import java.util.*

class CalendarViewModel(private val appContext: Context): ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>(listOf()) //(fakeTasksList())

    private val _filteredTasks = MutableLiveData<List<Task>>(listOf())
    val filteredTasks get() = _filteredTasks

    init {
        loadTasks()
    }

    //Parse Tasks from assets Json
    private fun loadTasks() = viewModelScope.launch {
        _tasks.value = loadTasksFromJSon(appContext)
        val today = Calendar.getInstance()
        //Filter before we pick a date, by today's date
        filterTasksByDay(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun filterTasksByDay(year: Int, month: Int, day: Int) {
        val month = month + 1
        //Filter tasks according to the picked date
        val startDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 00:00:00").time / 1000
        val endDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 23:59:59").time / 1000
        Log.d("CalendarViewModel", "Timestamp: $day $month $year $startDayTimestamp $endDayTimestamp")
        _filteredTasks.value = _tasks.value?.filter { task ->
            task.dateStart < endDayTimestamp && task.dateFinish > startDayTimestamp
        }
    }
}

fun fakeTasksList(): List<Task> = listOf(
    Task(1, "Task1", "Description1", 1619478000, 1619481599),
    Task(2, "Task2", "Description2", 1619510400, 1619524800)
)