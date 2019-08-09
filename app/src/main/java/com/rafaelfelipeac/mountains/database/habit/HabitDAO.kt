package com.rafaelfelipeac.mountains.database.habit

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.mountains.models.Habit

@Dao
interface HabitDAO {

    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE habitId = :habitId")
    fun get(habitId: Long): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(habit: Habit): Long

    @Delete
    fun delete(habit: Habit)
}