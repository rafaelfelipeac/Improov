package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAll(): List<ItemDataModel>

    @Query("SELECT * FROM item WHERE itemId = :itemId")
    fun get(itemId: Long): ItemDataModel

    @Insert(onConflict = REPLACE)
    fun save(itemDataModel: ItemDataModel): Long

    @Delete
    fun delete(itemDataModel: ItemDataModel)
}
