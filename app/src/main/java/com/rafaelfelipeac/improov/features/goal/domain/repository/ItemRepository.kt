package com.rafaelfelipeac.improov.features.goal.domain.repository

import com.rafaelfelipeac.improov.features.goal.domain.model.Item

interface ItemRepository {

    suspend fun getItems(): List<Item>

    suspend fun getItem(itemId: Long): Item

    suspend fun save(item: Item): Long

    suspend fun delete(item: Item)
}
