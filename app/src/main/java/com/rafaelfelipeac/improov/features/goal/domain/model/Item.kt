package com.rafaelfelipeac.improov.features.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    var goalId: Long,
    var name: String,
    var order: Int,
    var done: Boolean,
    var createdDate: Date? = null,
    var updatedDate:Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var deleteDate: Date? = null,
    val date: Date? = null): Serializable
