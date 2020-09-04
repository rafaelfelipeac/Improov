package com.rafaelfelipeac.improov.features.backup.data.model

import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel

data class Database(
    val language: String,
    val welcome: Boolean,
    val name: String,
    val firstTimeList: Boolean,
    val firstTimeAdd: Boolean,
    val goals: List<GoalDataModel>,
    val items: List<ItemDataModel>,
    val historics: List<HistoricDataModel>
)
