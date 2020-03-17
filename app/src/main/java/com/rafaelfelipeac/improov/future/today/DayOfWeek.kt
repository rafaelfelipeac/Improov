package com.rafaelfelipeac.improov.future.today

import com.rafaelfelipeac.improov.future.habit.Habit

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var list: MutableList<Habit> = mutableListOf()
)