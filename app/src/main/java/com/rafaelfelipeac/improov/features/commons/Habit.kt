package com.rafaelfelipeac.improov.features.commons

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "habit")
data class Habit (
    @PrimaryKey(autoGenerate = true)
    var habitId: Long = 0,
    var type: HabitType = HabitType.HAB_NONE,
    var periodType: PeriodType = PeriodType.PER_NONE,
    var periodDone: Int = 0,
    var periodTotal: Int = 0,
    var periodDaysBetween: Int = 0,
    var lastDate: Date? = null,
    var nextDate: Date? = null,
    var doneDate: Date? = null,
    var doneToday: Boolean = false,
    var weekDays: MutableList<Boolean> = mutableListOf(),
    var weekDaysLong: MutableList<Long> = mutableListOf()
) : Serializable, GoalHabit()

enum class HabitType { HAB_EVERYDAY, HAB_WEEKDAYS, HAB_PERIOD, HAB_CUSTOM, HAB_NONE }

enum class PeriodType { PER_WEEK, PER_MONTH, PER_YEAR, PER_CUSTOM, PER_NONE }
