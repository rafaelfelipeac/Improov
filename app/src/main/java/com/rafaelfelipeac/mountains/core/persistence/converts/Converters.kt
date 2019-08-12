package com.rafaelfelipeac.mountains.core.persistence.converts

import androidx.room.TypeConverter
import com.rafaelfelipeac.mountains.features.goal.GoalType
import com.rafaelfelipeac.mountains.features.habit.HabitType
import com.rafaelfelipeac.mountains.features.habit.PeriodType
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // date
    @TypeConverter fun timestampToDate(value: Long?): Date? { return value?.let { Date(it) } }

    @TypeConverter fun dateToTimestamp(date: Date?): Long? { return date?.time }

    // type
    @TypeConverter fun stringToGoalType(value: String): GoalType = GoalType.valueOf(value)

    @TypeConverter fun goalTypeToString(goalType: GoalType) = goalType.name

    // habitType
    @TypeConverter fun stringToHabitType(value: String): HabitType = HabitType.valueOf(value)

    @TypeConverter fun habitTypeToString(habitType: HabitType) = habitType.name

    // periodType
    @TypeConverter fun stringToPeriodType(value: String): PeriodType = PeriodType.valueOf(value)

    @TypeConverter fun periodTypeToString(periodType: PeriodType) = periodType.name

    // boolean
    @TypeConverter fun stringListToBooleanList(listOfDays: String): List<Boolean> { return Gson().
        fromJson(listOfDays, object : TypeToken<List<Boolean>>() {}.type) }

    @TypeConverter fun booleanListToStringList(listOfDays: List<Boolean>): String { return Gson().toJson(listOfDays) }

    // long
    @TypeConverter fun stringListToLongList(listOfDays: String): List<Long> { return Gson().
        fromJson(listOfDays, object : TypeToken<List<Long>>() {}.type) }

    @TypeConverter fun longListToStringList(listOfDays: List<Long>): String { return Gson().toJson(listOfDays) }
}