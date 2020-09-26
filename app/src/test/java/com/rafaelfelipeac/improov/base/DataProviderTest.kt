package com.rafaelfelipeac.improov.base

import com.google.gson.Gson
import com.rafaelfelipeac.improov.features.backup.data.model.Database
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModel
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import java.util.Calendar

object DataProviderTest {

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

    fun createItemDataModel(
        itemId: Long = 1L,
        goalId: Long = 1L,
        name: String = "item"
    ): ItemDataModel {
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

    fun createItem(
        itemId: Long = 1L,
        goalId: Long = 1L,
        name: String = "item",
        order: Int = 1
    ): Item {
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

    fun createHistoricDataModel(
        historicId: Long = 1L,
        goalId: Long = 1L,
        value: Float = 0f
    ): HistoricDataModel {
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

    @Suppress("LongParameterList")
    fun createJson(
        goals: List<GoalDataModel>,
        historics: List<HistoricDataModel>,
        items: List<ItemDataModel>,
        language: String,
        welcome: Boolean,
        name: String,
        firstTimeAdd: Boolean,
        firstTimeList: Boolean
    ): String {
        return Gson().toJson(
            Database(
                language,
                welcome,
                name,
                firstTimeList,
                firstTimeAdd,
                goals,
                items,
                historics
            )
        )
    }

    fun getDate() = Calendar.getInstance().timeInMillis
}
