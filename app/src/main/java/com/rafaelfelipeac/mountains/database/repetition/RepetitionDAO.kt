package com.rafaelfelipeac.mountains.database.repetition

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.mountains.models.Repetition

@Dao
interface RepetitionDAO {

    @Query("SELECT * FROM repetition")
    fun getAll(): LiveData<List<Repetition>>

    @Query("SELECT * FROM repetition WHERE repetitionId = :repetitionId")
    fun get(repetitionId: Long): LiveData<Repetition>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(repetition: Repetition): Long

    @Delete
    fun delete(repetition: Repetition)
}