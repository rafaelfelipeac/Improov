package com.rafaelfelipeac.improov.core.persistence.converts

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.data.enums.HabitType
import com.rafaelfelipeac.improov.features.commons.data.enums.PeriodType
import java.util.Date

class Converters {

    // Long
    @TypeConverter
    fun longMutableListToJson(value: MutableList<Long>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToLongMutableList(value: String) = Gson().fromJson(value, Array<Long>::class.java).toMutableList()

    // Boolean
    @TypeConverter
    fun booleanMutableListToJson(value: MutableList<Boolean>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToBooleanMutableList(value: String) = Gson().fromJson(value, Array<Boolean>::class.java).toMutableList()

    // Date
    @TypeConverter
    fun timestampToDate(value: Long?): Date? = value?.let(::Date)

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    // GoalType
    @TypeConverter
    fun stringToGoalType(value: String): GoalType = GoalType.valueOf(value)

    @TypeConverter
    fun goalTypeToString(goalType: GoalType) = goalType.name

    // HabitType
    @TypeConverter
    fun stringToHabitType(value: String): HabitType = HabitType.valueOf(value)

    @TypeConverter
    fun habitTypeToString(habitType: HabitType) = habitType.name

    // PeriodType
    @TypeConverter
    fun stringToPeriodType(value: String): PeriodType = PeriodType.valueOf(value)

    @TypeConverter
    fun periodTypeToString(periodType: PeriodType) = periodType.name
}
