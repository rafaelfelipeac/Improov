package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import java.util.Date

object ItemRules {

    fun create(
        goalId: Long,
        name: String,
        order: Int,
        date: Date
    ): Item {
        return Item(
            goalId = goalId,
            name = name,
            order = order,
            done = false,
            createdDate = date
        )
    }

    fun swapOrder(firstItem: Item, secondItem: Item) {
        val currentOrder = firstItem.order
        firstItem.order = secondItem.order
        secondItem.order = currentOrder
    }

    fun toggleDone(item: Item, date: Date): Item {
        item.done = !item.done

        if (item.done) {
            item.doneDate = date
        } else {
            item.undoneDate = date
        }

        return item
    }
}
