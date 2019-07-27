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
                discoverNextWeek(repetitionWeekDays)

                val list = repetitionWeekDaysLong.filter { it > 0L }.sortedBy { it }

                val dateToday = Calendar.getInstance()

                list.forEach {
                    val dateFor = Calendar.getInstance()
                    dateFor.timeInMillis = it

                    if (dateToday.get(Calendar.DAY_OF_WEEK) == dateFor.get(Calendar.DAY_OF_WEEK))
                        date.time = dateToday.time
                    else {
                        date.timeInMillis = list[0]
                    }
                }

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

fun Goal.discoverNextWeek(repetitionWeekDays: List<Boolean>) {
    repetitionWeekDaysLong = mutableListOf()

    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[0], Calendar.SUNDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[1], Calendar.MONDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[2], Calendar.TUESDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[3], Calendar.WEDNESDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[4], Calendar.THURSDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[5], Calendar.FRIDAY))
    repetitionWeekDaysLong.add(nextDayOfWeek(repetitionWeekDays[6], Calendar.SATURDAY))
}

fun nextDayOfWeek(selected: Boolean, day: Int): Long {
    if (selected) {
        val date = Calendar.getInstance()

        var diff = day - date.get(Calendar.DAY_OF_WEEK)

        if (diff <= 0) {
            diff += 7
        }

        date.add(Calendar.DAY_OF_MONTH, diff)
        date.setToMidnight()

        return date.timeInMillis
    } else {
        return 0
    }
}