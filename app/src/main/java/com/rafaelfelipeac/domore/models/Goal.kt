package com.rafaelfelipeac.domore.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var name: String,
    var medalValue: Float,
    var value: Float,
    var trophies: Boolean,
    var bronzeValue: Float,
    var silverValue: Float,
    var goldValue: Float,
    var incrementValue: Float,
    var decrementValue: Float,
    var initialDate: String,
    var finalDate: String,
    var type: Int,
    var done: Boolean,
    var order: Int): Serializable
