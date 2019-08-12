package com.rafaelfelipeac.mountains.features.goal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "historic")
data class Historic (
    @PrimaryKey(autoGenerate = true)
    var historicId: Long = 0,
    var goalId: Long = 0,
    var value: Float = 0F,
    var date: Date? = null): Serializable
