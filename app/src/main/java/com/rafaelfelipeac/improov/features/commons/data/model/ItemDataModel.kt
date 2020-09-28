package com.rafaelfelipeac.improov.features.commons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import java.io.Serializable
import java.util.Date
import javax.inject.Inject

@Entity(tableName = "item")
data class ItemDataModel(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long,
    val goalId: Long,
    var name: String,
    val order: Int,
    val done: Boolean,
    val createdDate: Date?,
    val updatedDate: Date?,
    val doneDate: Date?,
    val undoneDate: Date?,
    val deleteDate: Date?,
    val date: Date?
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
