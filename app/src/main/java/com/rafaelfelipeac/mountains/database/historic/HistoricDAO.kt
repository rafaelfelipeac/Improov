package com.rafaelfelipeac.mountains.database.historic

import androidx.room.*
import com.rafaelfelipeac.mountains.models.Historic

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): List<Historic>

    @Insert
    fun insertAll(historical: List<Historic>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historic: Historic)

    @Update
    fun update(historic: Historic)

    @Delete
    fun delete(historic: Historic)
}