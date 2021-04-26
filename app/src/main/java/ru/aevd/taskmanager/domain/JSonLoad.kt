package ru.aevd.taskmanager.domain

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.aevd.taskmanager.domain.entities.Task

private val jsonFormat = Json { ignoreUnknownKeys = true }

@Serializable
private class JsonTask(
    @SerialName("id")
    val id: Long,
    @SerialName("date_start")
    val dateStart: Long,
    @SerialName("date_finish")
    val dateFinish: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String
)

internal suspend fun loadTasks(context: Context): List<Task> = withContext(Dispatchers.IO) {
    val data = readAssetFileToString(context, "tasks.json")
    val jsonTasks = jsonFormat.decodeFromString<List<JsonTask>>(data)
    jsonTasks.map { jsonTask ->
        Task(
            id = jsonTask.id,
            name = jsonTask.name,
            description = jsonTask.description,
            dateStart = jsonTask.dateStart,
            dateFinish = jsonTask.dateFinish
        )
    }
}

private fun readAssetFileToString(context: Context, fileName: String): String {
    val stream = context.assets.open(fileName)
    return stream.bufferedReader().readText()
}


