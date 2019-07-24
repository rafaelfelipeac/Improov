package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.RepetitionType
import java.util.*

fun Goal.getPercentage() = if (divideAndConquer) { (value / goldValue) * 100 } else { (value / singleValue) * 100 }

fun Goal.isToday() = repetitionNextDate.isToday()

fun Goal.isLate() = repetitionNextDate.isLate()

fun Goal.isFuture() = repetitionNextDate.isFuture()

fun Goal.setNextRepetitionDate() {
    val date = Calendar.getInstance()

    repetitionNextDate =
        when (repetitionType) {
            RepetitionType.REP1 -> {
                date.addDays(0)

                date.time
            }
            RepetitionType.REP2 -> {
                date.addDays(1)

                date.time
            }
            RepetitionType.REP3 -> {
                date.addDays(1)

                date.time
            }
            RepetitionType.REP4 -> {
                date.addDays(1)

                date.time
            }
            else -> date.time
        }
}

fun Goal.addNextRepetitionDate(days: Int) {
    val date = Calendar.getInstance()

    date.addDays(days)
    repetitionNextDate = date.time
}