package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): List<HistoricDataModel>

    @Query("SELECT * FROM historic WHERE historicId = :historicId")
    fun get(historicId: Long): HistoricDataModel

    @Insert(onConflict = REPLACE)
    fun save(historicDataModel: HistoricDataModel): Long

    @Delete
    fun delete(historicDataModel: HistoricDataModel)
}
