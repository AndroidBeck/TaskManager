package ru.aevd.taskmanager.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.aevd.taskmanager.R
import ru.aevd.taskmanager.domain.entities.Task
import ru.aevd.taskmanager.domain.loadTasksFromJSon
import java.sql.Timestamp
import java.util.*

class CalendarViewModel(private val appContext: Context): ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>(listOf()) //(fakeTasksList())
    private val _filteredTasks = MutableLiveData<List<Task>>(listOf())
    val filteredTasks get() = _filteredTasks
    private val _state = MutableLiveData<State>(State.Loading)
    val state get() = _state

    init {
        loadTasks()
    }

    //Parse Tasks from assets Json
    private fun loadTasks() = viewModelScope.launch {
        _state.value = State.Loading
        try {
            _tasks.value = loadTasksFromJSon(appContext)
            if (_tasks.value.isNullOrEmpty())
                _state.value = State.Failed(appContext.getString(R.string.no_tasks_text))
            else {
                _state.value = State.Success
                //Filter before we pick a date, by today's date
                val today = Calendar.getInstance()
                filterTasksByDay(
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
                )
            }
        }
        catch (e: Exception) {
            _state.value = State.Failed(appContext.getString(R.string.error_loading_data_text))
        }
    }

    fun filterTasksByDay(year: Int, month: Int, day: Int) {
        if (_tasks.value.isNullOrEmpty()) return
        val month = month + 1
        _state.value = State.Loading
        //Filter tasks according to the picked date
        val startDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 00:00:00").time / 1000
        val endDayTimestamp: Long = Timestamp.valueOf("$year-$month-$day 23:59:59").time / 1000
        Log.d("CalendarViewModel", "Timestamp: $day $month $year $startDayTimestamp $endDayTimestamp")
        _filteredTasks.value = _tasks.value?.filter { task ->
            task.dateStart < endDayTimestamp && task.dateFinish > startDayTimestamp
        }
        if (_filteredTasks.value.isNullOrEmpty())
            _state.value = State.Failed(appContext.getString(R.string.no_tasks_by_day_text))
    }
}

@Suppress("unused")
fun fakeTasksList(): List<Task> = listOf(
    Task(1, "Task1", "Description1", 1619478000, 1619481599),
    Task(2, "Task2", "Description2", 1619510400, 1619524800)
)

sealed class State {
    object Loading : State()
    object Success : State()
    class Failed(val msg: String) : State()
}