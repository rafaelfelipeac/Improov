package com.rafaelfelipeac.mountains.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.mountains.features.goal.data.HistoricDAO
import com.rafaelfelipeac.mountains.features.goal.data.ItemDAO
import com.rafaelfelipeac.mountains.features.commons.data.UserDAO
import com.rafaelfelipeac.mountains.core.persistence.converts.Converters
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.goal.Historic
import com.rafaelfelipeac.mountains.features.goal.Item
import com.rafaelfelipeac.mountains.features.commons.data.GoalDAO
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.User
import com.rafaelfelipeac.mountains.features.commons.data.HabitDAO

@Database(entities = [Goal::class, Habit::class, Item::class, Historic::class, User::class], version = 46)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun goalDAO(): GoalDAO
    abstract fun habitDAO(): HabitDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun historicDAO(): HistoricDAO
    abstract fun userDAO(): UserDAO
}