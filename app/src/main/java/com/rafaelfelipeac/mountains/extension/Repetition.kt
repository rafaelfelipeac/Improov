package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.PeriodType
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.models.RepetitionType
import java.util.*

fun Repetition.isToday() = nextDate.isToday()

fun Repetition.isLate() = nextDate.isLate()

fun Repetition.isFuture() =  nextDate.isFuture()

fun Repetition.nextRepetitionDate() {
    val date = Calendar.getInstance()

    nextDate =
        when (type) {
            RepetitionType.REP1 -> {
                date.addDays(0)

                date.time
            }
            RepetitionType.REP2 -> {
                discoverNextWeek(weekDays)

                val list = weekDaysLong.filter { it > 0L }.sortedBy { it }

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

fun Repetition.nextRepetitionDateAfterDone() {
    val date = Calendar.getInstance()
    val today = Calendar.getInstance()

    today.setToMidnight()

    when (type) {
        RepetitionType.REP1 -> {
            if (nextDate.isLate()) {
                today.addDays(1)
                nextDate = today.time
            } else {
                nextDate.addDays(1)
            }
        }
        RepetitionType.REP2 -> {
            discoverNextWeek(weekDays)

            doneDate.setToMidnight()
            doneDate.setToMidnight()

            val list = weekDaysLong
                .filter { it > 0L && it > doneDate?.time!! && it > nextDate?.time!! }
                .sortedBy { it }

            if (list.isNotEmpty()) {
                date.timeInMillis = list[0]
            } else {
                val nextWeek = upOnNextWeek()

                val upRepetitionDoneDate = doneDate?.time?.let { Date(it) }
                val upRepetitionNextDate = nextDate?.time?.let { Date(it) }

                val upList = nextWeek
                    .filter { it > 0L && it > upRepetitionDoneDate?.time!! && it > upRepetitionNextDate?.time!! }
                    .sortedBy { it }

                if (upList.isNotEmpty()) {
                    date.timeInMillis = upList[0]
                }
            }

            nextDate = date.time
        }
        RepetitionType.REP3 -> {
            periodDone++

            if (periodDone == periodTotal) {
                setRepetitionNextCycle()
                periodDone = 0
            } else {
                if (nextDate.isLate()) {
                    today.addDays(1)
                    nextDate = today.time
                } else {
                    nextDate.addDays(1)
                }
            }
        }
        RepetitionType.REP4 -> {
            periodDone++

            setRepetitionNextCycle()
        }
        RepetitionType.REP_NONE -> TODO()
    }
}

fun Repetition.setRepetitionLastDate() {
    val calendar = Calendar.getInstance()

    when (this.periodType) {
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
            calendar.addDays(periodDaysBetween)
        }
        else -> {
            TODO()
        }
    }

    lastDate = calendar.time
}

fun Repetition.setRepetitionNextCycle() {
    val calendar = Calendar.getInstance()
    calendar.setToMidnight()

    when (this.periodType) {
        PeriodType.PER_WEEK -> {
            calendar.timeInMillis = nextDate?.time!!

            calendar.setToNext(Calendar.SUNDAY)
            nextDate = calendar.time

            calendar.setToNext(Calendar.SATURDAY)
            lastDate = calendar.time
        }
        PeriodType.PER_MONTH -> {
            calendar.timeInMillis = nextDate?.time!!

            calendar.firstDayOfMonth()
            nextDate = calendar.time

            calendar.lastDayOfMonth()
            lastDate = calendar.time
        }
        PeriodType.PER_YEAR -> {
            calendar.add(Calendar.YEAR, 1)

            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            nextDate = calendar.time

            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            lastDate = calendar.time
        }
        PeriodType.PER_CUSTOM -> {
            calendar.timeInMillis = lastDate?.time!!

            nextDate = calendar.time

            calendar.addDays(periodDaysBetween)

            lastDate = calendar.time
        }
        PeriodType.PER_NONE -> TODO()
    }
}

fun Repetition.discoverNextWeek(repetitionWeekDays: List<Boolean>) {
    weekDaysLong = mutableListOf()

    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[0], Calendar.SUNDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[1], Calendar.MONDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[2], Calendar.TUESDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[3], Calendar.WEDNESDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[4], Calendar.THURSDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[5], Calendar.FRIDAY))
    weekDaysLong.add(nextDayOfWeek(repetitionWeekDays[6], Calendar.SATURDAY))
}

fun Repetition.upOnNextWeek(): MutableList<Long> {
    val calendar = Calendar.getInstance()

    val list = mutableListOf<Long>()

    weekDaysLong.forEach {
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