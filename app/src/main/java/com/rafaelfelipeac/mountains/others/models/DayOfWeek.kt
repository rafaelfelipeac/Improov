package com.rafaelfelipeac.mountains.others.models

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var list: MutableList<GoalHabit> = mutableListOf()
)