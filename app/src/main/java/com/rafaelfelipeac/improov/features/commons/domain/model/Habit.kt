package com.rafaelfelipeac.improov.features.commons.domain.model

import com.rafaelfelipeac.improov.features.commons.data.enums.HabitType
import com.rafaelfelipeac.improov.features.commons.data.enums.PeriodType
import java.io.Serializable
import java.util.*

data class Habit (
    var habitId: Long = 0,
    var type: HabitType = HabitType.NONE,
    var periodType: PeriodType = PeriodType.NONE,
    var periodDone: Int = 0,
    var periodTotal: Int = 0,
    var periodDaysBetween: Int = 0,
    var lastDate: Date? = null,
    var nextDate: Date? = null,
    var doneDate: Date? = null,
    var doneToday: Boolean = false,
    var weekDays: MutableList<Boolean> = mutableListOf(),
    var weekDaysLong: MutableList<Long> = mutableListOf(),
    var order: Int = 0
) : Serializable
