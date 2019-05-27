package com.rafaelfelipeac.mountains.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,
    var uui: String = "",
    var name: String = "",
    var email: String = ""): Serializable