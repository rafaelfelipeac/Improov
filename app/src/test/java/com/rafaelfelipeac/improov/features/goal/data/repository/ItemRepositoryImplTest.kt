package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.goal.data.dao.ItemDAO
import com.rafaelfelipeac.improov.features.goal.data.model.ItemDataModelMapper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ItemRepositoryImplTest {

    @Mock
    internal lateinit var itemDAO: ItemDAO

    @Mock
    internal lateinit var itemDataModelMapper: ItemDataModelMapper

    private lateinit var itemRepositoryImp: ItemRepositoryImpl

    private val itemId = 1L

    @Before
    fun setup() {
        itemRepositoryImp = ItemRepositoryImpl(itemDAO, itemDataModelMapper)
    }

    @Test
    fun `GIVEN a itemId WHEN getItem is called THEN itemRepositoryImpl return a item with the specific itemId`() {
        runBlocking {
            // given
            val item = createItem(itemId).let { itemDataModelMapper.mapReverse(it) }
            given(itemDAO.get(itemId))
                .willReturn(item)

            // when
            val result = itemRepositoryImp.getItem(itemId)

            // then
            result equalTo item
        }
    }

    @Test
    fun `GIVEN a list of items WHEN getItems is called THEN itemRepositoryImpl return a list with the same items`() {
        runBlocking {
            // given
            val items = listOf(createItem(), createItem(), createItem())
                .let { itemDataModelMapper.mapListReverse(it) }
            given(itemDAO.getAll())
                .willReturn(items)

            // when
            val result = itemRepositoryImp.getItems()

            // then
            result equalTo items
        }
    }

    @Test
    fun `GIVEN a item WHEN save is called THEN itemRepositoryImp return the itemId as a confirmation`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            given(itemDAO.save(item.let { itemDataModelMapper.mapReverse(it) }))
                .willReturn(itemId)

            // when
            val result = itemRepositoryImp.save(item)

            // then
            result equalTo itemId
        }
    }

    @Test
    fun `GIVEN a item with a specific itemId WHEN delete is called THEN itemRepositoryImp return just a Unit value`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            val itemReverse = item.let { itemDataModelMapper.mapReverse(it) }
            doNothing().`when`(itemDAO).delete(itemReverse)

            // when
            val result = itemRepositoryImp.delete(item)

            // then
            result equalTo Unit
        }
    }
}
