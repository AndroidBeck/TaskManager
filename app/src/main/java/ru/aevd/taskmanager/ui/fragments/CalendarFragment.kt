package ru.aevd.taskmanager.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.aevd.taskmanager.R
import ru.aevd.taskmanager.databinding.CalendarFragmentBinding
import ru.aevd.taskmanager.domain.entities.Task
import ru.aevd.taskmanager.ui.adapters.TasksListAdapter
import ru.aevd.taskmanager.ui.viewmodels.CalendarViewModel
import ru.aevd.taskmanager.ui.viewmodels.State
import java.util.*

class CalendarFragment: Fragment(R.layout.calendar_fragment) {
    private lateinit var binding: CalendarFragmentBinding
    private val viewModel by lazy { CalendarViewModel(appContext = requireContext().applicationContext) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)
        setCalendar()
        setRecycler()
        viewModel.filteredTasks.observe(this.viewLifecycleOwner, this::updateTasksList)
        viewModel.state.observe(this.viewLifecycleOwner, this::updateStatus)
    }

    private fun setCalendar() {
        val today = Calendar.getInstance()
        binding.datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            //On picking date - filter tasks
            viewModel.filterTasksByDay(year, month, day)
        }
    }

    private fun setRecycler() {
        binding.tasksRecycler.adapter = TasksListAdapter()
        binding.tasksRecycler.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun updateTasksList(filteredTasks: List<Task>) {
        (binding.tasksRecycler.adapter as TasksListAdapter).bindTasks(filteredTasks)
        binding.tasksRecycler.isVisible = filteredTasks.isNotEmpty()
        binding.tasksStatusTextView.isVisible = filteredTasks.isEmpty()
    }

    private fun updateStatus(state: State) {
        binding.tasksRecycler.isVisible = state is State.Success
        when(state) {
            is State.Success -> {
                binding.tasksRecycler.isVisible = true
                binding.tasksStatusTextView.isVisible = false
            }
            is State.Failed -> {
                binding.tasksRecycler.isVisible = false
                binding.tasksStatusTextView.isVisible = true
                binding.tasksStatusTextView.text = state.msg
            }
            is State.Loading -> {}
        }
    }

}
