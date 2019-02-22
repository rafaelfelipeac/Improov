package com.rafaelfelipeac.domore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0,
    var goalId: Long = 0,
    var title: String = "",
    var desc: String = "",
    var author: String = "",
    var done: Boolean = false,
    var order: Int = 0): Serializable