package com.rafaelfelipeac.domore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var name: String = "",
    var medalValue: Float = 0F,
    var value: Float = 0F,
    var trophies: Boolean = false,
    var bronzeValue: Float = 0F,
    var silverValue: Float = 0F,
    var goldValue: Float = 0F,
    var incrementValue: Float = 0F,
    var decrementValue: Float = 0F,
    var initialDate: String = "",
    var finalDate: String = "",
    var type: Int = 0,
    var done: Boolean = false,
    var order: Int = 0): Serializable
