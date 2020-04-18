package com.rafaelfelipeac.improov.base

import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel
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

    fun createGoal(goalId: Long = 1L, name: String = "goal"): Goal {
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

    fun createItem(itemId: Long = 1L, goalId: Long = 1L, name: String = "item"): Item {
        return Item(
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

    fun createHistoric(historicId: Long = 1L, goalId: Long = 1L): Historic {
        return Historic(
            historicId = historicId,
            goalId = goalId,
            value = 0f,
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
