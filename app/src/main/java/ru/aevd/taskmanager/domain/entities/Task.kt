package ru.aevd.taskmanager.domain.entities

data class Task (
    val id: Long,
    val name: String,
    val description: String,
    val dateStart: Long,
    val dateFinish: Long
)