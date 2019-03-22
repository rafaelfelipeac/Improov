package com.rafaelfelipeac.mountains.database.item

import com.rafaelfelipeac.mountains.models.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDAO: ItemDAO) {

    fun getItems(): List<Item> {
        return itemDAO.getAll()
    }

    fun insert(item: Item) {
        return itemDAO.insert(item)
    }
}