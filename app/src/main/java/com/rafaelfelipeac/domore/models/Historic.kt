package com.rafaelfelipeac.domore.models

import java.io.Serializable
import java.util.*

data class Historic (
    var name: String = "",
    var value: Float = 0F,
    var date: Date? = null): Serializable
