package com.rafaelfelipeac.mountains.others.models

abstract class GoalHabit (
    var userId: Long = 0,
    var name: String = "",
    var order: Int = 0,
    var archived: Boolean = false
)