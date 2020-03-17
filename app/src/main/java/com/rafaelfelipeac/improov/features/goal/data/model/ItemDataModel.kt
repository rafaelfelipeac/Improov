package com.rafaelfelipeac.improov.features.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import java.io.Serializable
import java.util.*
import javax.inject.Inject

@Entity(tableName = "item")
data class ItemDataModel(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    var goalId: Long,
    var name: String,
    var order: Int,
    var done: Boolean,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var deleteDate: Date? = null,
    val date: Date? = null
) : Serializable

class ItemDataModelMapper @Inject constructor() : TwoWayMapper<ItemDataModel, Item> {

    override fun map(param: ItemDataModel): Item = with(param) {
        Item(
            itemId = itemId,
            goalId = goalId,
            name = name,
            order = order,
            done = done,
            createdDate = createdDate,
            updatedDate = updatedDate,
            doneDate = doneDate,
            undoneDate = undoneDate,
            deleteDate = deleteDate,
            date = date
        )
    }

    override fun mapReverse(param: Item): ItemDataModel = with(param) {
        ItemDataModel(
            itemId = itemId,
            goalId = goalId,
            name = name,
            order = order,
            done = done,
            createdDate = createdDate,
            updatedDate = updatedDate,
            doneDate = doneDate,
            undoneDate = undoneDate,
            deleteDate = deleteDate,
            date = date
        )
    }
}
