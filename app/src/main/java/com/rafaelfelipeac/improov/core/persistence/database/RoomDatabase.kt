package com.rafaelfelipeac.improov.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.improov.features.goal.data.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.ItemDAO
import com.rafaelfelipeac.improov.core.persistence.converts.Converters
import com.rafaelfelipeac.improov.features.commons.Goal
import com.rafaelfelipeac.improov.features.goal.Historic
import com.rafaelfelipeac.improov.features.goal.Item
import com.rafaelfelipeac.improov.features.commons.data.GoalDAO
import com.rafaelfelipeac.improov.features.commons.Habit
import com.rafaelfelipeac.improov.features.commons.data.HabitDAO

@Database(
    entities = [Goal::class, Habit::class, Item::class, Historic::class],
    version = 47,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun goalDAO(): GoalDAO
    abstract fun habitDAO(): HabitDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun historicDAO(): HistoricDAO
}
