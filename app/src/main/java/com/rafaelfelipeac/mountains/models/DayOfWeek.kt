package com.rafaelfelipeac.mountains.models

data class DayOfWeek(
    var weekDay: String = "",
    val monthDay: String = "",
    var routines: MutableList<Routine> = mutableListOf()
)