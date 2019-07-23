package com.rafaelfelipeac.mountains.models.converts

import androidx.room.TypeConverter
import com.rafaelfelipeac.mountains.models.GoalType

class GoalTypeConverters {
    @TypeConverter fun stringToGoalType(value: String): GoalType = GoalType.valueOf(value)

    @TypeConverter fun goalTypeToString(goalType: GoalType) = goalType.name
}