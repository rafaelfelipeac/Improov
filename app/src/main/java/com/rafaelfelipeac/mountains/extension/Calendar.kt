package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.DayOfWeek
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

fun Calendar.getNextWeek(): List<DayOfWeek> {
    val today = Calendar.getInstance()

    val days = mutableListOf<DayOfWeek>()

    for (i in 0..6) {
        today.addDays(1)

        days.add(DayOfWeek(today.getDayOfWeek(today.get(Calendar.DAY_OF_WEEK)), today.time.format().toString()))
    }

    return days
}

fun Calendar.getDayOfWeek(day: Int): String {
    return when (day) {
        1 -> "Domingo"
        2 -> "Segunda-feira"
        3 -> "Terça-feira"
        4 -> "Quarta-feira"
        5 -> "Quinta-feira"
        6 -> "Sexta-feira"
        7 -> "Sábado"
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