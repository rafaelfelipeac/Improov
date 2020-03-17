package com.rafaelfelipeac.improov.features.goal.domain.model

import java.io.Serializable
import java.util.*

data class Item(
    val itemId: Long = 0,
    var goalId: Long,
    var name: String,
    var order: Int,
    var done: Boolean,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var deleteDate: Date? = null,
    val date: Date? = null
) : Serializable
