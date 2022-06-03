package com.viktor.kh.dev.shoplist.helpers

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val format = SimpleDateFormat("dd.MM.yyyy")

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    return format.parse(date).time
}