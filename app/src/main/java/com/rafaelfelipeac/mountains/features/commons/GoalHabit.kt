package com.rafaelfelipeac.mountains.features.commons

abstract class GoalHabit (
    var userId: Long = 0,
    var name: String = "",
    var order: Int = 0,
    var archived: Boolean = false
)
