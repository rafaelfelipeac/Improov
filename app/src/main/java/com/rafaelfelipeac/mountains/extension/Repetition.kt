package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.PeriodType
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.RoutineType
import java.util.*

fun Routine.isToday() = nextDate.isToday()

fun Routine.isLate() = nextDate.isLate()

fun Routine.isFuture() =  nextDate.isFuture()

fun Routine.nextRoutineDate() {
    val date = Calendar.getInstance()

    nextDate =
        when (type) {
            RoutineType.ROUT_1 -> {
                date.addDays(0)

                date.time
            }
            RoutineType.ROUT_2 -> {
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
            RoutineType.ROUT_3 -> {
                date.addDays(0)

                date.time
            }
            RoutineType.ROUT_4 -> {
                date.addDays(0)

                date.time
            }
            else -> date.time
        }
}

fun Routine.nextRoutineDateAfterDone() {
    val date = Calendar.getInstance()
    val today = Calendar.getInstance()

    today.setToMidnight()

    when (type) {
        RoutineType.ROUT_1 -> {
            if (nextDate.isLate()) {
                today.addDays(1)
                nextDate = today.time
            } else {
                nextDate.addDays(1)
            }
        }
        RoutineType.ROUT_2 -> {
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

                val upRoutineDoneDate = doneDate?.time?.let { Date(it) }
                val upRoutineNextDate = nextDate?.time?.let { Date(it) }

                val upList = nextWeek
                    .filter { it > 0L && it > upRoutineDoneDate?.time!! && it > upRoutineNextDate?.time!! }
                    .sortedBy { it }

                if (upList.isNotEmpty()) {
                    date.timeInMillis = upList[0]
                }
            }

            nextDate = date.time
        }
        RoutineType.ROUT_3 -> {
            periodDone++

            if (periodDone == periodTotal) {
                setRoutineNextCycle()
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
        RoutineType.ROUT_4 -> {
            periodDone++

            setRoutineNextCycle()
        }
        RoutineType.ROUT_NONE -> TODO()
    }
}

fun Routine.setRoutineLastDate() {
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

fun Routine.setRoutineNextCycle() {
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

fun Routine.discoverNextWeek(routineWeekDays: List<Boolean>) {
    weekDaysLong = mutableListOf()

    weekDaysLong.add(nextDayOfWeek(routineWeekDays[0], Calendar.SUNDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[1], Calendar.MONDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[2], Calendar.TUESDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[3], Calendar.WEDNESDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[4], Calendar.THURSDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[5], Calendar.FRIDAY))
    weekDaysLong.add(nextDayOfWeek(routineWeekDays[6], Calendar.SATURDAY))
}

fun Routine.upOnNextWeek(): MutableList<Long> {
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