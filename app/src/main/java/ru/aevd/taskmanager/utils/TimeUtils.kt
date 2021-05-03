package ru.aevd.taskmanager.utils

import android.text.format.DateFormat
import java.util.*

fun convertTimestampToString(timestamp: Long): String {
    val milliseconds = timestamp * 1000
    return DateFormat.format("dd/MM/yyyy HH:mm", Date(milliseconds)).toString()
}