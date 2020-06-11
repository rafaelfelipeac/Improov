package com.rafaelfelipeac.improov.features.goal.domain.model

import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import java.io.Serializable
import java.util.Date

data class Goal(
    var goalId: Long = 0,
    var name: String = "",
    var value: Float = 0F,
    var type: GoalType = GoalType.GOAL_NONE,
    var done: Boolean = false,
    var divideAndConquer: Boolean = false,
    var singleValue: Float = 0F,
    var bronzeValue: Float = 0F,
    var silverValue: Float = 0F,
    var goldValue: Float = 0F,
    var order: Int = 0,
    var archived: Boolean = false,
    var incrementValue: Float = 0F,
    var decrementValue: Float = 0F,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var archiveDate: Date? = null,
    var date: Date? = null
) : Serializable
