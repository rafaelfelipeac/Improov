package com.rafaelfelipeac.mountains.extension

import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun Date.convertDateToString(): String {
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

    return format.format(this)
}

fun Date?.isToday() = DateUtils.isToday(this!!.time)

fun Date?.isLate() = this!! < Calendar.getInstance().time

fun Date?.isFuture() = this!! > Calendar.getInstance().time

fun Date?.format() = DateFormat.format("dd MMM",   this)!!
