package com.rafaelfelipeac.improov.base

import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import junit.framework.TestCase

object DataProvider {

    fun createGoalDataModel(goalId: Long = 1L, name: String = "goal"): GoalDataModel {
        return GoalDataModel(
            goalId = goalId,
            name = name,
            value = 5f,
            type = GoalType.GOAL_LIST,
            done = false,
            divideAndConquer = false,
            singleValue = 10f,
            bronzeValue = 0f,
            silverValue = 0f,
            goldValue = 0f,
            order = 1,
            archived = false,
            incrementValue = 0f,
            decrementValue = 0f,
            createdDate = null,
            updatedDate = null,
            doneDate = null,
            undoneDate = null,
            archiveDate = null,
            date = null
        )
    }

    fun createGoal(goalId: Long = 1L, name: String = "goal", order: Int = 1): Goal {
        return Goal(
            goalId = goalId,
            name = name,
            value = 5f,
            type = GoalType.GOAL_LIST,
            done = false,
            divideAndConquer = false,
            singleValue = 10f,
            bronzeValue = 0f,
            silverValue = 0f,
            goldValue = 0f,
            order = order,
            archived = false,
            incrementValue = 0f,
            decrementValue = 0f,
            createdDate = null,
            updatedDate = null,
            doneDate = null,
            undoneDate = null,
            archiveDate = null,
            date = null
        )
    }

    fun createItemDataModel(itemId: Long = 1L, goalId: Long = 1L, name: String = "item"): ItemDataModel {
        return ItemDataModel(
            itemId = itemId,
            goalId = goalId,
            name = name,
            order = 1,
            done = false,
            createdDate = null,
            updatedDate = null,
            doneDate = null,
            undoneDate = null,
            deleteDate = null,
            date = null
        )
    }

    fun createItem(itemId: Long = 1L, goalId: Long = 1L, name: String = "item", order: Int = 1): Item {
        return Item(
            itemId = itemId,
            goalId = goalId,
            name = name,
            order = order,
            done = false,
            createdDate = null,
            updatedDate = null,
            doneDate = null,
            undoneDate = null,
            deleteDate = null,
            date = null
        )
    }

    fun createHistoricDataModel(historicId: Long = 1L, goalId: Long = 1L, value: Float = 0f): HistoricDataModel {
        return HistoricDataModel(
            historicId = historicId,
            goalId = goalId,
            value = value,
            date = null
        )
    }

    fun createHistoric(historicId: Long = 1L, goalId: Long = 1L, value: Float = 0f): Historic {
        return Historic(
            historicId = historicId,
            goalId = goalId,
            value = value,
            date = null
        )
    }

    infix fun <T> T.shouldBeEqualTo(expected: T?): T = this.apply {
        TestCase.assertEquals(
            expected,
            this
        )
    }
}
