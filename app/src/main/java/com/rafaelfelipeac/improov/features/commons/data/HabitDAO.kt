package com.rafaelfelipeac.improov.features.commons.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.improov.features.commons.Habit

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
