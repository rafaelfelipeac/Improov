package com.rafaelfelipeac.improov.future.today

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var list: MutableList<Habit> = mutableListOf()
)
