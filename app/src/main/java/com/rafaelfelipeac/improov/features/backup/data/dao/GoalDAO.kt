package com.rafaelfelipeac.improov.features.backup.data.dao

import androidx.room.*
import com.rafaelfelipeac.improov.features.goal.data.model.GoalDataModel

@Dao
interface GoalDAO {

    @Query("SELECT * FROM goal")
    fun getAll(): List<GoalDataModel>

    @Query("SELECT * FROM goal WHERE goalId = :goalId")
    fun get(goalId: Long): GoalDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(goalDataModel: GoalDataModel): Long

    @Delete
    fun delete(goalDataModel: GoalDataModel)
}
