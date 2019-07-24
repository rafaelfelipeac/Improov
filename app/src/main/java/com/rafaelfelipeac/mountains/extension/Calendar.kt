package com.rafaelfelipeac.mountains.extension

import java.util.*

fun Calendar.addDays(days: Int) {
    add(Calendar.DAY_OF_YEAR, days)
    setToMidnight()
}

fun Calendar.setToMidnight() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}
