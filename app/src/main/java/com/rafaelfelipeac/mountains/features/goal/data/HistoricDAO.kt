package com.rafaelfelipeac.mountains.core.database.historic

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.rafaelfelipeac.mountains.models.Historic

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): LiveData<List<Historic>>

    @Query("SELECT * FROM historic WHERE historicId = :historicId")
    fun get(historicId: Long): LiveData<Historic>

    @Insert(onConflict = REPLACE)
    fun save(historic: Historic): Long

    @Delete
    fun delete(historic: Historic)
}