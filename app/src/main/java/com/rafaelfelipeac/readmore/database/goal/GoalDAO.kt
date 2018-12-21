package com.rafaelfelipeac.readmore.database.goal

import android.arch.persistence.room.*
import com.rafaelfelipeac.readmore.models.Goal

@Dao
interface GoalDAO {

    @Query("SELECT * FROM goal")
    fun getAll(): List<Goal>

    @Insert
    fun insertAll(goals: List<Goal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goal: Goal)

    @Update
    fun update(goal: Goal)

    @Delete
    fun delete(goal: Goal)
}