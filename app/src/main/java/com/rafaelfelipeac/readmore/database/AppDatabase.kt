package com.rafaelfelipeac.readmore.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rafaelfelipeac.readmore.database.goal.GoalDAO
import com.rafaelfelipeac.readmore.models.Goal

@Database(entities = [Goal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDAO(): GoalDAO
}