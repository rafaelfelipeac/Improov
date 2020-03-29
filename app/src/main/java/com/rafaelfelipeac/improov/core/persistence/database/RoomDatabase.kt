package com.rafaelfelipeac.improov.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.improov.core.persistence.converts.Converters
import com.rafaelfelipeac.improov.features.goal.data.dao.GoalDAO
import com.rafaelfelipeac.improov.features.goal.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.dao.ItemDAO
import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel

const val DATABASE_VERSION_48 = 48 // Release 1.4.0
const val DATABASE_VERSION_49 = 49 // Release 1.5.0

@Database(
    entities = [GoalDataModel::class, ItemDataModel::class, HistoricDataModel::class],
    version = 49,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun goalDAO(): GoalDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun historicDAO(): HistoricDAO
}
