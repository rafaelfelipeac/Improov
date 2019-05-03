package com.rafaelfelipeac.mountains.database.historic

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.rafaelfelipeac.mountains.models.Historic

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): LiveData<List<Historic>>

    @Insert(onConflict = REPLACE)
    fun save(historic: Historic): Long

    @Delete
    fun delete(historic: Historic)
}