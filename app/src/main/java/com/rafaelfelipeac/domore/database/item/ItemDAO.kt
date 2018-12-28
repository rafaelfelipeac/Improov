package com.rafaelfelipeac.domore.database.item

import android.arch.persistence.room.*
import com.rafaelfelipeac.domore.models.Item

@Dao
interface ItemDAO {

    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Insert
    fun insertAll(items: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}