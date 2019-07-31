package com.rafaelfelipeac.mountains.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "goal")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var goalId: Long = 0,
    var userId: Long = 0,
    var name: String = "",
    var value: Float = 0F,
    var order: Int = 0,
    var done: Boolean = false,
    var archived: Boolean = false,
    var goalType: GoalType = GoalType.INVALID,
    var repetition: Boolean = false,
    var repetitionType: RepetitionType = RepetitionType.REP_NONE,
    var repetitionPeriodType: PeriodType = PeriodType.PER_NONE,
    var repetitionPeriodDone: Int = 0,
    var repetitionPeriodTotal: Int = 0,
    var repetitionPeriodDaysBetween: Int = 0,
    var repetitionLastDate: Date? = null,
    var repetitionNextDate: Date? = null,
    var repetitionDoneDate: Date? = null,
    var repetitionDoneToday: Boolean = false,
    var repetitionWeekDays: MutableList<Boolean> = mutableListOf(),
    var repetitionWeekDaysLong: MutableList<Long> = mutableListOf(),
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
): Serializable

enum class GoalType { LIST, COUNTER, FINAL_VALUE, INVALID }

enum class RepetitionType { REP1, REP2, REP3, REP4, REP_NONE }

enum class PeriodType { PER_WEEK, PER_MONTH, PER_YEAR, PER_CUSTOM, PER_NONE }