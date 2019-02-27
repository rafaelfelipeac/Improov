package com.rafaelfelipeac.domore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var name: String = "",
    var value: Float = 0F,
    var order: Int = 0,
    var done: Boolean = false,
    var type: Int = 0,
    var trophies: Boolean = false,
    var medalValue: Float = 0F,
    var bronzeValue: Float = 0F,
    var silverValue: Float = 0F,
    var goldValue: Float = 0F,
    var incrementValue: Float = 0F,
    var decrementValue: Float = 0F,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var deleteDate: Date? = null,
    var finalDate: Date? = null): Serializable
