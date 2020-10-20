package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao,
    private val itemDataModelMapper: ItemDataModelMapper
) : ItemRepository {

    override suspend fun getItems(): List<Item> {
        return withContext(Dispatchers.IO) {
            itemDao.getAll()
                .map { itemDataModelMapper.map(it) }
        }
    }

    override suspend fun getItem(itemId: Long): Item {
        return withContext(Dispatchers.IO) {
            itemDao.get(itemId)
                .let { itemDataModelMapper.map(it) }
        }
    }

    override suspend fun save(item: Item): Long {
        return withContext(Dispatchers.IO) {
            itemDao.save(item
                .let { itemDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(item: Item) {
        return withContext(Dispatchers.IO) {
            itemDao.delete(item
                .let { itemDataModelMapper.mapReverse(it) })
        }
    }
}
