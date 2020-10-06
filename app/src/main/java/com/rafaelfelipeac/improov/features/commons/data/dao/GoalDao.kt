package com.rafaelfelipeac.improov.features.commons.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModel

@Dao
interface GoalDao {

    @Query("SELECT * FROM goal")
    fun getAll(): List<GoalDataModel>

    @Query("SELECT * FROM goal WHERE goalId = :goalId")
    fun get(goalId: Long): GoalDataModel

    @Insert(onConflict = REPLACE)
    fun save(goalDataModel: GoalDataModel): Long

    @Delete
    fun delete(goalDataModel: GoalDataModel)
}
