package com.rafaelfelipeac.mountains.database.goal

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.rafaelfelipeac.mountains.models.Goal

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