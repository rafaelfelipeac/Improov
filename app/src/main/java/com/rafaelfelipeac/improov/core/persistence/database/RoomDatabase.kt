package com.rafaelfelipeac.improov.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.improov.core.persistence.converts.Converters
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModel
import com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModel

const val DATABASE_VERSION_48 = 48 // 1.4.0
const val DATABASE_VERSION_49 = 49 // 1.5.0

@Database(
    entities = [GoalDataModel::class, ItemDataModel::class, HistoricDataModel::class],
    version = 49,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun itemDao(): ItemDao
    abstract fun historicDao(): HistoricDao
}
