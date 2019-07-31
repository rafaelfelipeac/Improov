package com.rafaelfelipeac.mountains.models

data class DayOfWeek(
    var title1: String = "",
    val title2: String = "",
    var list: MutableList<Goal> = mutableListOf()
)