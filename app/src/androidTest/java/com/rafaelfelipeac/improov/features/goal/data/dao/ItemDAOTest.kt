package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createItemDataModel
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDAOTest : BaseInstTest() {

    private val itemDAO get() = roomDatabase.itemDAO()

    @Test
    fun givenANewItemWhenGetIsCalledThenTheSameItemIsReturned() {
        // given
        val itemId = 1L
        val item = createItemDataModel(itemId)

        itemDAO.save(item)

        // when
        val result = itemDAO.get(itemId)

        // then
        result shouldBeEqualTo item
    }

    @Test
    fun givenTwoNewItemsWhenGetAllIsCalledThenAListWithTheTwoItemsAreReturned() {
        // given
        val itemA = createItemDataModel(itemId = 1, name = "itemA")
        val itemB = createItemDataModel(itemId = 2, name = "itemB")
        val list = listOf(itemA, itemB)

        itemDAO.save(itemA)
        itemDAO.save(itemB)

        // when
        val result = itemDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenANewItemWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val item = createItemDataModel()

        itemDAO.save(item)

        // when
        val result = itemDAO.delete(item)

        // then
        result shouldBeEqualTo Unit
    }

    @Test
    fun givenTwoNewItemsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneItemIsReturned() {
        // given
        val itemA = createItemDataModel(itemId = 1, name = "itemA")
        val itemB = createItemDataModel(itemId = 2, name = "itemB")
        val list = mutableListOf(itemA, itemB)

        itemDAO.save(itemA)
        itemDAO.save(itemB)

        list.remove(itemA)
        itemDAO.delete(itemA)

        // when
        val result = itemDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAItemWhenGetIsCalledThenItMustReturnedTheItemUpdated() {
        // given
        val itemId = 10L
        val item = createItemDataModel(itemId = itemId, name = "itemA")
        itemDAO.save(item)

        item.name = "itemB"
        itemDAO.save(item)

        // when
        val result = itemDAO.get(itemId)

        // then
        result shouldBeEqualTo item
    }
}
