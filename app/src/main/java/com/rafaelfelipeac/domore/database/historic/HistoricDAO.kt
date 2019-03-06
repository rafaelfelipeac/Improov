package com.rafaelfelipeac.domore.database.historic

import androidx.room.*
import com.rafaelfelipeac.domore.models.Historic

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): List<Historic>

    @Insert
    fun insertAll(historics: List<Historic>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historic: Historic)

    @Update
    fun update(historic: Historic)

    @Delete
    fun delete(historic: Historic)
}