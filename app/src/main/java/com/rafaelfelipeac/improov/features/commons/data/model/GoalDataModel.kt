package com.rafaelfelipeac.improov.features.commons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import java.io.Serializable
import java.util.Date
import javax.inject.Inject

@Entity(tableName = "goal")
data class GoalDataModel(
    @PrimaryKey(autoGenerate = true)
    val goalId: Long,
    var name: String,
    val value: Float,
    val type: GoalType,
    val done: Boolean,
    val divideAndConquer: Boolean,
    val singleValue: Float,
    val bronzeValue: Float,
    val silverValue: Float,
    val goldValue: Float,
    val order: Int,
    val archived: Boolean,
    val incrementValue: Float,
    val decrementValue: Float,
    val createdDate: Date?,
    val updatedDate: Date?,
    val doneDate: Date?,
    val undoneDate: Date?,
    val archiveDate: Date?,
    val date: Date?
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
