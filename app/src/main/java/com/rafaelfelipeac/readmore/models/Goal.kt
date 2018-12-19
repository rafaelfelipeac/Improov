package com.rafaelfelipeac.readmore.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "goal")
class Goal(
    @PrimaryKey
    val name: String,
    val totalRead: Int,
    val actualRead: Int,
    val initialDate: String,
    val finalDate: String)
