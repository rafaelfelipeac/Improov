package com.rafaelfelipeac.mountains.core.extension

import android.content.Context
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.features.today.DayOfWeek
import java.util.*

fun Calendar.addDays(days: Int) {
    add(Calendar.DAY_OF_YEAR, days)
    setToMidnight()
}

fun Calendar.setToMidnight() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.getNextWeek(context: Context): List<DayOfWeek> {
    val today = Calendar.getInstance()

    val days = mutableListOf<DayOfWeek>()

    for (i in 0..6) {
        today.addDays(1)

        days.add(
            DayOfWeek(
                today.getDayOfWeek(today.get(Calendar.DAY_OF_WEEK), context),
                today.time.format().toString()
            )
        )
    }

    return days
}

fun Calendar.getDayOfWeek(day: Int, context: Context): String {
    return when (day) {
        1 -> context.getString(R.string.calendar_sunday)
        2 -> context.getString(R.string.calendar_monday)
        3 -> context.getString(R.string.calendar_tuesday)
        4 -> context.getString(R.string.calendar_wednesday)
        5 -> context.getString(R.string.calendar_thursday)
        6 -> context.getString(R.string.calendar_friday)
        7 -> context.getString(R.string.calendar_saturday)
        else -> ""
    }
}

fun Calendar.firstDayOfMonth() {
    add(Calendar.MONTH, 1)
    set(Calendar.DATE, 1)
}

fun Calendar.lastDayOfMonth() {
    add(Calendar.MONTH, 1)
    set(Calendar.DATE, 1)
    add(Calendar.DATE, -1)
}

fun Calendar.setToNext(day: Int) {
    var diff = day - get(Calendar.DAY_OF_WEEK)

    if (diff <= 0) {
        diff += 7
    }

    add(Calendar.DAY_OF_MONTH, diff)
    setToMidnight()
}