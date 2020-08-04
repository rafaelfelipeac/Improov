package com.rafaelfelipeac.improov.base

import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import junit.framework.TestCase

object DataProviderAndroidTest {

    fun createGoalDataModel(goalId: Long = 1L, name: String = "goal1"): GoalDataModel {
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

    fun createHistoricDataModel(historicId: Long = 1L, goalId: Long = 1L): HistoricDataModel {
        return HistoricDataModel(
            historicId = historicId,
            goalId = goalId,
            value = 0f,
            date = null
        )
    }
}
