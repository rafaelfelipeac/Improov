package com.rafaelfelipeac.mountains.models

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var habits: MutableList<Habit> = mutableListOf()
)