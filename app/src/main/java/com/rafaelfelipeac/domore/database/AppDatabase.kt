package com.rafaelfelipeac.domore.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rafaelfelipeac.domore.database.goal.GoalDAO
import com.rafaelfelipeac.domore.models.Goal

@Database(entities = [Goal::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDAO(): GoalDAO
}