package com.rafaelfelipeac.improov.features.backup.data.model

import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModel

data class Database(
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val appVersion: String,
    val settings: Settings,
    val goals: List<GoalDataModel>,
    val items: List<ItemDataModel>,
    val historics: List<HistoricDataModel>,
) {
    data class Settings(
        val language: String,
        val welcome: Boolean,
        val name: String,
        val firstTimeList: Boolean,
        val firstTimeAdd: Boolean,
        val themeMode: String? = null,
    )

    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
    }
}
