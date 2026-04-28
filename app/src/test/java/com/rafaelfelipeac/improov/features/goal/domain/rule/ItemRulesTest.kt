package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class ItemRulesTest {

    @Test
    fun `create initializes the item as not done`() {
        val date = Date(123L)

        val item = ItemRules.create(
            goalId = 7L,
            name = "Read",
            order = 2,
            date = date
        )

        item.goalId equalTo 7L
        item.name equalTo "Read"
        item.order equalTo 2
        item.done equalTo false
        item.createdDate equalTo date
    }

    @Test
    fun `swapOrder exchanges the orders`() {
        val firstItem = Item(goalId = 1L, name = "A", order = 1, done = false, createdDate = null)
        val secondItem = Item(goalId = 1L, name = "B", order = 2, done = false, createdDate = null)

        ItemRules.swapOrder(firstItem, secondItem)

        firstItem.order equalTo 2
        secondItem.order equalTo 1
    }

    @Test
    fun `toggleDone marks the item done with a timestamp`() {
        val item = Item(goalId = 1L, name = "A", order = 1, done = false, createdDate = null)
        val date = Date(456L)

        ItemRules.toggleDone(item, date)

        assertTrue(item.done)
        item.doneDate equalTo date
        item.undoneDate equalTo null
    }

    @Test
    fun `toggleDone marks the item undone with a timestamp`() {
        val item = Item(goalId = 1L, name = "A", order = 1, done = true, createdDate = null)
        val date = Date(789L)

        ItemRules.toggleDone(item, date)

        assertFalse(item.done)
        item.doneDate equalTo null
        item.undoneDate equalTo date
    }
}
