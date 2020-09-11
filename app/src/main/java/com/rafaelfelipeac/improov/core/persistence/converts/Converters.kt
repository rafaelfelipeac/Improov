package com.rafaelfelipeac.improov.core.persistence.converts

import androidx.room.TypeConverter
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import java.util.Date

class Converters {

    // date
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // goalType
    @TypeConverter
    fun stringToGoalType(value: String): GoalType = GoalType.valueOf(value)

    @TypeConverter
    fun goalTypeToString(goalType: GoalType) = goalType.name
}
