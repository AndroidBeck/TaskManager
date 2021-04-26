package ru.aevd.taskmanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.aevd.taskmanager.R
import ru.aevd.taskmanager.domain.entities.Task

class TasksListAdapter: RecyclerView.Adapter<TaskViewHolder>() {
    private var tasks: List<Task> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun bindTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}

class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val time: TextView = itemView.findViewById(R.id.tv_time)
    private val taskName: TextView = itemView.findViewById(R.id.tv_task_name)

    //TODO: pass time parameter
    fun onBind(task: Task) {
        time.text = "12:00"
        taskName.text = task.name
    }

}