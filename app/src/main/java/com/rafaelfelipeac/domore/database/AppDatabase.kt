package com.rafaelfelipeac.domore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafaelfelipeac.domore.database.goal.GoalDAO
import com.rafaelfelipeac.domore.database.historic.HistoricDAO
import com.rafaelfelipeac.domore.database.item.ItemDAO
import com.rafaelfelipeac.domore.models.Converts.Converters
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Historic
import com.rafaelfelipeac.domore.models.Item

@Database(entities = [Goal::class, Item::class, Historic::class], version = 22)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDAO(): GoalDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun historicDAO(): HistoricDAO

}