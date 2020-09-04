package com.rafaelfelipeac.improov.features.backup.data.dao

import androidx.room.*
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModel

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAll(): List<ItemDataModel>

    @Query("SELECT * FROM item WHERE itemId = :itemId")
    fun get(itemId: Long): ItemDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(itemDataModel: ItemDataModel): Long

    @Delete
    fun delete(itemDataModel: ItemDataModel)
}
