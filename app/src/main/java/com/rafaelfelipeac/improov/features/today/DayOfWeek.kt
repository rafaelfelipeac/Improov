package com.rafaelfelipeac.improov.features.today

import com.rafaelfelipeac.improov.features.commons.GoalHabit

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var list: MutableList<GoalHabit> = mutableListOf()
)