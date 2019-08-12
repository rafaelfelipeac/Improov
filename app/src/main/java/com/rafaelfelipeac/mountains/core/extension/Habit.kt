package com.rafaelfelipeac.mountains.core.extension

import com.rafaelfelipeac.mountains.features.habit.Habit
import com.rafaelfelipeac.mountains.features.habit.HabitType
import com.rafaelfelipeac.mountains.features.habit.PeriodType
import java.util.*

fun Habit.isToday() = nextDate.isToday()

fun Habit.isLate() = nextDate.isLate()

fun Habit.isFuture() =  nextDate.isFuture()

fun Habit.nextHabitDate() {
    val date = Calendar.getInstance()

    nextDate =
        when (type) {
            HabitType.HAB_EVERYDAY -> {
                date.addDays(0)

                date.time
            }
            HabitType.HAB_WEEKDAYS -> {
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
            HabitType.HAB_PERIOD -> {
                date.addDays(0)

                date.time
            }
            HabitType.HAB_CUSTOM -> {
                date.addDays(0)

                date.time
            }
            else -> date.time
        }
}

fun Habit.nextHabitDateAfterDone() {
    val date = Calendar.getInstance()
    val today = Calendar.getInstance()

    today.setToMidnight()

    when (type) {
        HabitType.HAB_EVERYDAY -> {
            if (nextDate.isLate()) {
                today.addDays(1)
                nextDate = today.time
            } else {
                nextDate.addDays(1)
            }
        }
        HabitType.HAB_WEEKDAYS -> {
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

                val upHabitDoneDate = doneDate?.time?.let { Date(it) }
                val upHabitNextDate = nextDate?.time?.let { Date(it) }

                val upList = nextWeek
                    .filter { it > 0L && it > upHabitDoneDate?.time!! && it > upHabitNextDate?.time!! }
                    .sortedBy { it }

                if (upList.isNotEmpty()) {
                    date.timeInMillis = upList[0]
                }
            }

            nextDate = date.time
        }
        HabitType.HAB_PERIOD -> {
            periodDone++

            if (periodDone == periodTotal) {
                setHabitNextCycle()
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
        HabitType.HAB_CUSTOM -> {
            periodDone++

            setHabitNextCycle()
        }
        HabitType.HAB_NONE -> TODO()
    }
}

fun Habit.setHabitLastDate() {
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

fun Habit.setHabitNextCycle() {
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

fun Habit.discoverNextWeek(habitWeekDays: List<Boolean>) {
    weekDaysLong = mutableListOf()

    weekDaysLong.add(nextDayOfWeek(habitWeekDays[0], Calendar.SUNDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[1], Calendar.MONDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[2], Calendar.TUESDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[3], Calendar.WEDNESDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[4], Calendar.THURSDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[5], Calendar.FRIDAY))
    weekDaysLong.add(nextDayOfWeek(habitWeekDays[6], Calendar.SATURDAY))
}

fun Habit.upOnNextWeek(): MutableList<Long> {
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