package com.rafaelfelipeac.mountains.database.goal

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.mountains.models.Goal

@Dao
interface GoalDAO {

    @Query("SELECT * FROM goal")
    fun getAll(): LiveData<List<Goal>>

    @Query("SELECT * FROM goal WHERE goalId = :goalId")
    fun get(goalId: Long): LiveData<Goal>

    @Insert
    fun insertAll(goals: List<Goal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goal: Goal)

    @Update
    fun update(goal: Goal)

    @Delete
    fun delete(goal: Goal)
}