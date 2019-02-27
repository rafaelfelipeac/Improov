package com.rafaelfelipeac.domore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0,
    var goalId: Long = 0,
    var title: String = "",
    var order: Int = 0,
    var done: Boolean = false,
    var createdDate: Date? = null,
    var updatedDate:Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var deleteDate: Date? = null,
    var finalDate: Date? = null): Serializable