package com.rafaelfelipeac.improov.features.goal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "historic")
data class Historic (
    @PrimaryKey(autoGenerate = true)
    val historicId: Long = 0,
    val goalId: Long,
    val value: Float,
    val date: Date?): Serializable
