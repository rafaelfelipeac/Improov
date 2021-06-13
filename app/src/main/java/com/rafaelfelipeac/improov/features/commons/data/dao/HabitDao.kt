package com.rafaelfelipeac.improov.features.commons.data.dao

import androidx.room.*
import com.rafaelfelipeac.improov.features.commons.data.model.HabitDataModel

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAll(): List<HabitDataModel>

    @Query("SELECT * FROM habit WHERE habitId = :habitId")
    fun get(habitId: Long): HabitDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(habitDataModel: HabitDataModel): Long

    @Delete
    fun delete(habitDataModel: HabitDataModel)
}
