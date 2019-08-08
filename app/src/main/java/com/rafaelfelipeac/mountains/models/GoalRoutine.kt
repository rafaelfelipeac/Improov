package com.rafaelfelipeac.mountains.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

abstract class GoalRoutine(
    var userId: Long = 0,
    var name: String = "",
    var order: Int = 0,
    var archived: Boolean = false
)

@Entity(tableName = "goal")
data class Goal (
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var value: Float = 0F,
    var type: GoalType = GoalType.INVALID,
    var done: Boolean = false,
    var divideAndConquer: Boolean = false,
    var singleValue: Float = 0F,
    var bronzeValue: Float = 0F,
    var silverValue: Float = 0F,
    var goldValue: Float = 0F,
    var incrementValue: Float = 0F,
    var decrementValue: Float = 0F,
    var createdDate: Date? = null,
    var updatedDate: Date? = null,
    var doneDate: Date? = null,
    var undoneDate: Date? = null,
    var archiveDate: Date? = null,
    var finalDate: Date? = null
) : Serializable, GoalRoutine()

@Entity(tableName = "routine")
data class Routine (
    @PrimaryKey(autoGenerate = true)
    var routineId: Long = 0,
    var type: RoutineType = RoutineType.ROUT_NONE,
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
) : Serializable, GoalRoutine()

enum class RoutineType { ROUT_1, ROUT_2, ROUT_3, ROUT_4, ROUT_NONE }

enum class PeriodType { PER_WEEK, PER_MONTH, PER_YEAR, PER_CUSTOM, PER_NONE }

enum class GoalType { LIST, COUNTER, FINAL_VALUE, INVALID }