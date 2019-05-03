package com.rafaelfelipeac.mountains.database.item

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDAO: ItemDAO) {

    fun getItems(): LiveData<List<Item>> {
        return itemDAO.getAll()
    }

    fun save(item: Item): Long {
        return itemDAO.save(item)
    }

    fun delete(item: Item) {
        return itemDAO.delete(item)
    }
}