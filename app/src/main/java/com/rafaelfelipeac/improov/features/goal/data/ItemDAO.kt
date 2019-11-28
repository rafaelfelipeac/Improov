package com.rafaelfelipeac.improov.features.goal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rafaelfelipeac.improov.features.goal.Item

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE itemId = :itemId")
    fun get(itemId: Long): LiveData<Item>

    @Insert(onConflict = REPLACE)
    fun save(item: Item): Long

    @Delete
    fun delete(item: Item)
}
