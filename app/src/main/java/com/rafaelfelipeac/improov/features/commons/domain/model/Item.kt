package com.rafaelfelipeac.improov.features.commons.domain.model

import java.io.Serializable
import java.util.Date

data class Item(
    val itemId: Long = 0,
    val goalId: Long,
    var name: String,
    var order: Int,
    var done: Boolean,
    val createdDate: Date?,
    var updatedDate: Date? = null,
    val doneDate: Date? = null,
    var undoneDate: Date? = null,
    val deleteDate: Date? = null,
    val date: Date? = null
) : Serializable
