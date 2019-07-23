package com.rafaelfelipeac.mountains.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.mountains.database.goal.GoalDAO
import com.rafaelfelipeac.mountains.database.historic.HistoricDAO
import com.rafaelfelipeac.mountains.database.item.ItemDAO
import com.rafaelfelipeac.mountains.database.user.UserDAO
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.models.converts.DateConverters
import com.rafaelfelipeac.mountains.models.converts.GoalTypeConverters
import com.rafaelfelipeac.mountains.models.converts.RepetitionConverters

@Database(entities = [Goal::class, Item::class, Historic::class, User::class], version = 29)
@TypeConverters(DateConverters::class, GoalTypeConverters::class, RepetitionConverters::class)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun goalDAO(): GoalDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun historicDAO(): HistoricDAO
    abstract fun userDAO(): UserDAO

}