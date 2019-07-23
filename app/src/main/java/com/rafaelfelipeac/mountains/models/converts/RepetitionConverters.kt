package com.rafaelfelipeac.mountains.models.converts

import androidx.room.TypeConverter
import com.rafaelfelipeac.mountains.models.RepetitionType

class RepetitionConverters {
    @TypeConverter fun stringToRepetitionType(value: String): RepetitionType = RepetitionType.valueOf(value)

    @TypeConverter fun repetitionTypeToString(repetitionType: RepetitionType) = repetitionType.name
}