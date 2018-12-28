package com.rafaelfelipeac.domore.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long? = 0,
    val goalId: Long? = 0,
    val title: String,
    val desc: String,
    val author: String)