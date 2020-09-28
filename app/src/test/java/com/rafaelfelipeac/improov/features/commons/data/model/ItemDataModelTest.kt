package com.rafaelfelipeac.improov.features.commons.data.model

import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.base.DataProviderTest.createItemDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ItemDataModelTest {

    private val mapper = ItemDataModelMapper()

    @Test
    fun `GIVEN itemDataModel WHEN map is called THEN a item is returned`() {
        // given
        val itemDataModel = createItemDataModel()

        // when
        val item = itemDataModel.let { mapper.map(itemDataModel) }

        // then
        item equalTo createItem()
    }

    @Test
    fun `GIVEN itemDataModel WHEN map is called THEN a item with the same values is returned`() {
        // given
        val itemDataModel = createItemDataModel(itemId = 123, name = "item1")

        // when
        val item = itemDataModel.let { mapper.map(itemDataModel) }

        // then
        item equalTo createItem(itemId = 123, name = "item1")
    }

    @Test
    fun `GIVEN item WHEN mapReverse is called THEN itemDataModel is returned`() {
        // given
        val item = createItem()

        // when
        val itemDataModel = item.let { mapper.mapReverse(item) }

        // then
        itemDataModel equalTo createItemDataModel()
    }

    @Test
    fun `GIVEN item WHEN mapReverse is called THEN itemDataModel with the same values is returned`() {
        // given
        val item = createItem(itemId = 123, name = "item1")

        // when
        val itemDataModel = item.let { mapper.mapReverse(item) }

        // then
        itemDataModel equalTo createItemDataModel(
            itemId = 123,
            name = "item1"
        )
    }
}
