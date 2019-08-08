package com.rafaelfelipeac.mountains.database.routine

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.mountains.models.Routine

@Dao
interface RoutineDAO {

    @Query("SELECT * FROM routine")
    fun getAll(): LiveData<List<Routine>>

    @Query("SELECT * FROM routine WHERE routineId = :routineId")
    fun get(routineId: Long): LiveData<Routine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(routine: Routine): Long

    @Delete
    fun delete(routine: Routine)
}