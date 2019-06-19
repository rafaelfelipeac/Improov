package com.rafaelfelipeac.mountains.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.convertDateToString(): String {
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

    return format.format(this)
}