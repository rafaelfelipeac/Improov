package com.rafaelfelipeac.improov.features.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import java.io.Serializable
import java.util.*
import javax.inject.Inject

@Entity(tableName = "goal")
data class GoalDataModel(
    @PrimaryKey(autoGenerate = true)
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

class GoalDataModelMapper @Inject constructor() : TwoWayMapper<GoalDataModel, Goal> {

    override fun map(param: GoalDataModel): Goal = with(param) {
        Goal(
            goalId = goalId,
            name = name,
            value = value,
            type = type,
            done = done,
            divideAndConquer = divideAndConquer,
            singleValue = singleValue,
            bronzeValue = bronzeValue,
            silverValue = silverValue,
            goldValue = goldValue,
            order = order,
            archived = archived,
            incrementValue = incrementValue,
            decrementValue = decrementValue,
            createdDate = createdDate,
            updatedDate = updatedDate,
            doneDate = doneDate,
            undoneDate = undoneDate,
            archiveDate = archiveDate,
            date = date
        )
    }

    override fun mapReverse(param: Goal): GoalDataModel = with(param) {
        GoalDataModel(
            goalId = goalId,
            name = name,
            value = value,
            type = type,
            done = done,
            divideAndConquer = divideAndConquer,
            singleValue = singleValue,
            bronzeValue = bronzeValue,
            silverValue = silverValue,
            goldValue = goldValue,
            order = order,
            archived = archived,
            incrementValue = incrementValue,
            decrementValue = decrementValue,
            createdDate = createdDate,
            updatedDate = updatedDate,
            doneDate = doneDate,
            undoneDate = undoneDate,
            archiveDate = archiveDate,
            date = date
        )
    }
}
