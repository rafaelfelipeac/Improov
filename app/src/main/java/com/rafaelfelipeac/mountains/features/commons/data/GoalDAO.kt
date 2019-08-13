package com.rafaelfelipeac.mountains.features.commons.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.mountains.features.commons.Goal

@Dao
interface GoalDAO {

    @Query("SELECT * FROM goal")
    fun getAll(): LiveData<List<Goal>>

    @Query("SELECT * FROM goal WHERE goalId = :goalId")
    fun get(goalId: Long): LiveData<Goal>

    @Insert(onConflict = REPLACE)
    fun save(goal: Goal): Long

    @Delete
    fun delete(goal: Goal)
}