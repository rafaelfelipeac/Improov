package com.rafaelfelipeac.improov.features.commons.domain.model

import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import java.io.Serializable
import java.util.Date

const val PERCENTAGE_MAX = 100

data class Goal(
    val goalId: Long = 0,
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
    val doneDate: Date? = null,
    var undoneDate: Date? = null,
    val archiveDate: Date? = null,
    val date: Date? = null
) : Serializable {

    fun getPercentage() = if (divideAndConquer) {
        (value / goldValue) * PERCENTAGE_MAX
    } else {
        (value / singleValue) * PERCENTAGE_MAX
    }
}
