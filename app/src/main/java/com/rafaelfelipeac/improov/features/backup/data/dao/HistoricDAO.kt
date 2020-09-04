package com.rafaelfelipeac.improov.features.backup.data.dao

import androidx.room.*
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModel

@Dao
interface HistoricDAO {

    @Query("SELECT * FROM historic")
    fun getAll(): List<HistoricDataModel>

    @Query("SELECT * FROM historic WHERE historicId = :historicId")
    fun get(historicId: Long): HistoricDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(historicDataModel: HistoricDataModel): Long

    @Delete
    fun delete(historicDataModel: HistoricDataModel)
}
