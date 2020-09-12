package com.rafaelfelipeac.improov.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.rafaelfelipeac.improov.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun Date.convertDateToString(context: Context): String = SimpleDateFormat(
    context.getString(R.string.date_format_full)
).format(this)

fun Date?.isToday() = this?.time?.let(DateUtils::isToday) ?: false

fun Date?.isLate(): Boolean {
    this ?: return false
    return this < Calendar.getInstance().time
}

fun Date?.isFuture(): Boolean {
    this ?: return false
    return this > Calendar.getInstance().time
}

fun Date?.format(context: Context) = DateFormat.format(
    context.getString(R.string.date_format_dd_MMM),
    this
) ?: ""

fun Date?.addDays(days: Int) {
    this ?: return

    val calendar = getCalendar(time).apply {
        add(Calendar.DAY_OF_YEAR, days)
    }

    time = getTime(calendar)

    setToMidnight()
}

fun Date?.setToMidnight() {
    this ?: return

    val calendar = getCalendar(time).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    time = getTime(calendar)
}

fun getCalendar(time: Long): Calendar = Calendar.getInstance().apply {
    timeInMillis = time
}

fun getTime(calendar: Calendar): Long = calendar.timeInMillis
