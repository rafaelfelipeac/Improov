package com.rafaelfelipeac.mountains.features.today

import com.rafaelfelipeac.mountains.features.commons.GoalHabit

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var list: MutableList<GoalHabit> = mutableListOf()
)