package com.rafaelfelipeac.mountains.features.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.features.goal.data.ItemDAO
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDAO: ItemDAO) {

    fun getItems(): LiveData<List<Item>> {
        return itemDAO.getAll()
    }

    fun getItem(itemId: Long): LiveData<Item> {
        return itemDAO.get(itemId)
    }

    fun save(item: Item): Long {
        return itemDAO.save(item)
    }

    fun delete(item: Item) {
        return itemDAO.delete(item)
    }
}