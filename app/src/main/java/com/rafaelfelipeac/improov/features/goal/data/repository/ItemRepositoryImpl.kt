package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.features.goal.data.dao.ItemDAO
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModelMapper
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDAO: ItemDAO,
    private val itemDataModelMapper: ItemDataModelMapper
) : ItemRepository {

    override suspend fun getItems(): List<Item> {
//        return withContext(Dispatchers.IO) {
//            itemDAO.getAll()
//        }

        return itemDAO.getAll()
            .map { itemDataModelMapper.map(it) }
    }

    override suspend fun getItem(itemId: Long): Item {
//        return withContext(Dispatchers.IO) {
//            itemDAO.get(itemId)
//        }

        return itemDAO.get(itemId)
            .let { itemDataModelMapper.map(it) }
    }

    override suspend fun save(item: Item): Long {
//        return withContext(Dispatchers.IO) {
//            itemDAO.save(item)
//        }

        return itemDAO.save(item
            .let { itemDataModelMapper.mapReverse(it) })
    }

    override suspend fun delete(item: Item) {
//        return withContext(Dispatchers.IO) {
//            itemDAO.delete(item)
//        }

        return itemDAO.delete(item
            .let { itemDataModelMapper.mapReverse(it) })
    }
}
