package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.PeriodType
import com.rafaelfelipeac.mountains.models.RepetitionType
import java.util.*

fun Goal.getPercentage() = if (divideAndConquer) {
    (value / goldValue) * 100
} else {
    (value / singleValue) * 100
}

fun Goal.isToday() = repetitionNextDate.isToday()

fun Goal.isLate() = repetitionNextDate.isLate()

fun Goal.isFuture() = repetitionNextDate.isFuture()

fun Goal.nextRepetitionDate() {
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
                date.addDays(0)

                date.time
            }
            RepetitionType.REP4 -> {
                date.addDays(0)

                date.time
            }
            else -> date.time
        }
}

fun Goal.nextRepetitionDateAfterDone() {
    val date = Calendar.getInstance()
    val today = Calendar.getInstance()

    today.setToMidnight()

    when (repetitionType) {
        RepetitionType.REP1 -> {
            repetitionNextDate.addDays(1)
        }
        RepetitionType.REP2 -> {
            discoverNextWeek(repetitionWeekDays)

            repetitionDoneDate.setToMidnight()
            repetitionNextDate.setToMidnight()

            val list = repetitionWeekDaysLong
                .filter { it > 0L && it > repetitionDoneDate?.time!! && it > repetitionNextDate?.time!! }
                .sortedBy { it }

            if (list.isNotEmpty()) {
                date.timeInMillis = list[0]
            } else {
                val nextWeek = upOnNextWeek()

                val upRepetitionDoneDate = repetitionDoneDate?.time?.let { Date(it) }
                val upRepetitionNextDate = repetitionNextDate?.time?.let { Date(it) }

                val upList = nextWeek
                    .filter { it > 0L && it > upRepetitionDoneDate?.time!! && it > upRepetitionNextDate?.time!! }
                    .sortedBy { it }

                if (upList.isNotEmpty()) {
                    date.timeInMillis = upList[0]
                }
            }

            repetitionNextDate = date.time
        }
        RepetitionType.REP3 -> {
            repetitionPeriodDone++

            if (repetitionPeriodDone == repetitionPeriodTotal) {
                setRepetitionNextCycle()
                repetitionPeriodDone = 0
            } else {
                repetitionNextDate?.addDays(1)
            }
        }
        RepetitionType.REP4 -> {
            repetitionPeriodDone++

            setRepetitionNextCycle()
        }
        RepetitionType.REP_NONE -> TODO()
    }
}

fun Goal.setRepetitionLastDate() {
    val calendar = Calendar.getInstance()

    when (this.repetitionPeriodType) {
        PeriodType.PER_WEEK -> {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            calendar.setToMidnight()
        }
        PeriodType.PER_MONTH -> {
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            calendar.setToMidnight()
        }
        PeriodType.PER_YEAR -> {
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            calendar.setToMidnight()
        }
        PeriodType.PER_CUSTOM -> {
            calendar.addDays(repetitionPeriodDaysBetween)
        }
        else -> {
            TODO()
        }
    }

    repetitionLastDate = calendar.time
}

fun Goal.setRepetitionNextCycle() {
    val calendar = Calendar.getInstance()
    calendar.setToMidnight()

    when (this.repetitionPeriodType) {
        PeriodType.PER_WEEK -> {
            calendar.timeInMillis = repetitionNextDate?.time!!

            calendar.setToNext(Calendar.SUNDAY)
            repetitionNextDate = calendar.time

            calendar.setToNext(Calendar.SATURDAY)
            repetitionLastDate = calendar.time
        }
        PeriodType.PER_MONTH -> {
            calendar.timeInMillis = repetitionNextDate?.time!!

            calendar.firstDayOfMonth()
            repetitionNextDate = calendar.time

            calendar.lastDayOfMonth()
            repetitionLastDate = calendar.time
        }
        PeriodType.PER_YEAR -> {
            calendar.add(Calendar.YEAR, 1)

            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            repetitionNextDate = calendar.time

            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            repetitionLastDate = calendar.time
        }
        PeriodType.PER_CUSTOM -> {
            calendar.timeInMillis = repetitionLastDate?.time!!

            repetitionNextDate = calendar.time

            calendar.addDays(repetitionPeriodDaysBetween)

            repetitionLastDate = calendar.time
        }
        PeriodType.PER_NONE -> TODO()
    }
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

fun Goal.upOnNextWeek(): MutableList<Long> {
    val calendar = Calendar.getInstance()

    val list = mutableListOf<Long>()

    repetitionWeekDaysLong.forEach {
        calendar.timeInMillis = it
        calendar.addDays(7)

        list.add(calendar.timeInMillis)
    }

    return list
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