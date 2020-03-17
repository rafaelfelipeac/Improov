package com.rafaelfelipeac.improov.features.goal.domain.usecase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal

@Dao
internal interface GoalDAO {

    @Query("SELECT * FROM goal")
    fun getAll(): List<Goal>

    @Query("SELECT * FROM goal WHERE goalId = :goalId")
    fun get(goalId: Long): Goal

    @Insert(onConflict = REPLACE)
    fun save(goal: Goal): Long

    @Delete
    fun delete(goal: Goal)
}
