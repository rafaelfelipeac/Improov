package com.rafaelfelipeac.improov.features.commons.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createItemDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDaoTest : BaseInstTest() {

    private val itemDao get() = roomDatabase.itemDao()

    @Test
    fun givenANewItemWhenGetIsCalledThenTheSameItemIsReturned() {
        // given
        val itemId = 1L
        val item = createItemDataModel(itemId)

        itemDao.save(item)

        // when
        val result = itemDao.get(itemId)

        // then
        result equalTo item
    }

    @Test
    fun givenTwoNewItemsWhenGetAllIsCalledThenAListWithTheTwoItemsAreReturned() {
        // given
        val itemA = createItemDataModel(itemId = 1, name = "itemA")
        val itemB = createItemDataModel(itemId = 2, name = "itemB")
        val list = listOf(itemA, itemB)

        itemDao.save(itemA)
        itemDao.save(itemB)

        // when
        val result = itemDao.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenANewItemWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val item = createItemDataModel()

        itemDao.save(item)

        // when
        val result = itemDao.delete(item)

        // then
        result equalTo Unit
    }

    @Test
    fun givenTwoNewItemsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneItemIsReturned() {
        // given
        val itemA = createItemDataModel(itemId = 1, name = "itemA")
        val itemB = createItemDataModel(itemId = 2, name = "itemB")
        val list = mutableListOf(itemA, itemB)

        itemDao.save(itemA)
        itemDao.save(itemB)

        list.remove(itemA)
        itemDao.delete(itemA)

        // when
        val result = itemDao.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAItemWhenGetIsCalledThenItMustReturnedTheItemUpdated() {
        // given
        val itemId = 10L
        val item = createItemDataModel(itemId = itemId, name = "itemA")
        itemDao.save(item)

        item.name = "itemB"
        itemDao.save(item)

        // when
        val result = itemDao.get(itemId)

        // then
        result equalTo item
    }
}
