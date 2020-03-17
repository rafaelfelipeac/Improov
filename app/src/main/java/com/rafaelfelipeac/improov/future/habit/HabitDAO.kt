package com.rafaelfelipeac.improov.future.habit

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDAO {

//    @Query("SELECT * FROM habit")
//    fun getAll(): LiveData<List<Habit>>
//
//    @Query("SELECT * FROM habit WHERE habitId = :habitId")
//    fun get(habitId: Long): LiveData<Habit>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun save(habit: Habit): Long
//
//    @Delete
//    fun delete(habit: Habit)
}
