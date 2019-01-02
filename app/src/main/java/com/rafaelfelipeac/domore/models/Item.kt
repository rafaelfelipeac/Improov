package com.rafaelfelipeac.domore.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long? = 0,
    var goalId: Long? = 0,
    var title: String,
    var desc: String,
    var author: String)