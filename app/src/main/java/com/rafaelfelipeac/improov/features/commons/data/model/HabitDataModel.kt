package com.rafaelfelipeac.improov.features.commons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.commons.data.enums.HabitType
import com.rafaelfelipeac.improov.features.commons.data.enums.PeriodType
import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import java.io.Serializable
import java.util.*
import javax.inject.Inject

@Entity(tableName = "habit")
data class HabitDataModel(
    @PrimaryKey(autoGenerate = true)
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

class HabitDataModelMapper @Inject constructor() : TwoWayMapper<HabitDataModel, Habit> {

    override fun map(param: HabitDataModel): Habit = with(param) {
        Habit(
            habitId = habitId,
            type = type,
            periodType = periodType,
            periodDone = periodDone,
            periodTotal = periodTotal,
            periodDaysBetween = periodDaysBetween,
            lastDate = lastDate,
            nextDate = nextDate,
            doneDate = doneDate,
            doneToday = doneToday,
            weekDays = weekDays,
            weekDaysLong = weekDaysLong,
            order = order
        )
    }

    override fun mapReverse(param: Habit): HabitDataModel = with(param) {
        HabitDataModel(
            habitId = habitId,
            type = type,
            periodType = periodType,
            periodDone = periodDone,
            periodTotal = periodTotal,
            periodDaysBetween = periodDaysBetween,
            lastDate = lastDate,
            nextDate = nextDate,
            doneDate = doneDate,
            doneToday = doneToday,
            weekDays = weekDays,
            weekDaysLong = weekDaysLong,
            order = order
        )
    }
}
