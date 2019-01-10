package com.rafaelfelipeac.domore.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rafaelfelipeac.domore.database.goal.GoalDAO
import com.rafaelfelipeac.domore.database.item.ItemDAO
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Item

@Database(entities = [Goal::class, Item::class], version = 15)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDAO(): GoalDAO
    abstract fun itemDAO(): ItemDAO

}