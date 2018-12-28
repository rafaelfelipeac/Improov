package com.rafaelfelipeac.domore.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val goalId: Long = 0,
    val name: String,
    val totalRead: Int,
    val actualRead: Int,
    val initialDate: String,
    val finalDate: String,
    var type: Int): Serializable
