package com.rafaelfelipeac.mountains.models.converts

import androidx.room.TypeConverter
import com.rafaelfelipeac.mountains.models.GoalType
import com.rafaelfelipeac.mountains.models.RepetitionType
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // date
    @TypeConverter fun timestampToDate(value: Long?): Date? { return value?.let { Date(it) } }

    @TypeConverter fun dateToTimestamp(date: Date?): Long? { return date?.time }

    // goalType
    @TypeConverter fun stringToGoalType(value: String): GoalType = GoalType.valueOf(value)

    @TypeConverter fun goalTypeToString(goalType: GoalType) = goalType.name

    // repetitionType
    @TypeConverter fun stringToRepetitionType(value: String): RepetitionType = RepetitionType.valueOf(value)

    @TypeConverter fun repetitionTypeToString(repetitionType: RepetitionType) = repetitionType.name

    // boolean
    @TypeConverter fun stringListToBooleanList(listOfDays: String): List<Boolean> { return Gson().
        fromJson(listOfDays, object : TypeToken<List<Boolean>>() {}.type) }

    @TypeConverter fun booleanListToStringList(listOfDays: List<Boolean>): String { return Gson().toJson(listOfDays) }

    // long
    @TypeConverter fun stringListToLongList(listOfDays: String): List<Long> { return Gson().
        fromJson(listOfDays, object : TypeToken<List<Long>>() {}.type) }

    @TypeConverter fun longListToStringList(listOfDays: List<Long>): String { return Gson().toJson(listOfDays) }
}