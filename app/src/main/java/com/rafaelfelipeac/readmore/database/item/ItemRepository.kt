package com.rafaelfelipeac.readmore.database.item

import com.rafaelfelipeac.readmore.models.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDAO: ItemDAO) {

    fun getItems(): List<Item> {
        return itemDAO.getAll()
    }

    fun insert(item: Item) {
        return itemDAO.insert(item)
    }
}