package com.rafaelfelipeac.mountains.core.extension

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

fun Date?.addDays(days: Int) {
    val calendar = getCalendar(this?.time!!)

    calendar.add(Calendar.DAY_OF_YEAR, days)

    time = getTime(calendar)

    setToMidnight()
}

fun Date?.setToMidnight() {
    val calendar = getCalendar(this?.time!!)

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    time = getTime(calendar)
}

fun getCalendar(time: Long): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time

    return calendar
}

fun getTime(calendar: Calendar): Long {
    return calendar.timeInMillis
}
