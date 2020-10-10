package com.rafaelfelipeac.improov.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.rafaelfelipeac.improov.R
import java.util.Date
import java.util.Calendar

@SuppressLint("SimpleDateFormat")

fun Date?.formatToDayMonth(context: Context) = DateFormat.format(
    context.getString(R.string.date_format_day_month), this
) ?: ""

fun Date?.formatToDate(context: Context) = DateFormat.format(
    context.getString(R.string.date_format_date), this
) ?: ""

fun Date.formatToDateTime(context: Context) = DateFormat.format(
    context.getString(R.string.date_format_date_time), this
) ?: ""

fun Date?.isToday() = this?.time?.let(DateUtils::isToday) ?: false

fun Date?.isLate(): Boolean {
    this ?: return false
    return this < Calendar.getInstance().time
}

fun Date?.isFuture(): Boolean {
    this ?: return false
    return this > Calendar.getInstance().time
}

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
