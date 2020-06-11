package com.rafaelfelipeac.improov.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.rafaelfelipeac.improov.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
fun Date.convertDateToString(context: Context): String {
    val format = SimpleDateFormat(context.getString(R.string.date_format_full))

    return format.format(this)
}

fun Date?.isToday() = DateUtils.isToday(this!!.time)

fun Date?.isLate() = this!! < Calendar.getInstance().time

fun Date?.isFuture() = this!! > Calendar.getInstance().time

fun Date?.format(context: Context) =
    DateFormat.format(context.getString(R.string.date_format_dd_MMM), this)!!

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
