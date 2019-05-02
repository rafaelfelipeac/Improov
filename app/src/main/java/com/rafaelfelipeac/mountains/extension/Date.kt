package com.rafaelfelipeac.mountains.extension

import java.text.DateFormat
import java.util.*

fun Date.convertDateToString(): String {
    val instance = DateFormat.getInstance()

    return instance.format(this)
}