package com.ken.cinema.util

import java.text.SimpleDateFormat
import java.util.*

private fun dateFormat(): SimpleDateFormat {

    return SimpleDateFormat(
        "EEE, d/MM/yyyy",
        Locale.getDefault()
    )
}

fun String.simpleDateFormat() : String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date = format.parse(this)

    return dateFormat().format(date)
}



fun Long.convertLongToTime(): String {
    val date = Date(this)
    val format = dateFormat()
    return format.format(date)
}
