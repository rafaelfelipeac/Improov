package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import java.util.Date

object HistoricRules {

    fun createIncrement(goalId: Long, value: Float, date: Date): Historic {
        return Historic(
            goalId = goalId,
            value = value,
            date = date
        )
    }

    fun createDecrement(goalId: Long, value: Float, date: Date): Historic {
        return Historic(
            goalId = goalId,
            value = value * -1,
            date = date
        )
    }

    fun createManual(goalId: Long, value: Float, date: Date): Historic {
        return Historic(
            goalId = goalId,
            value = value,
            date = date
        )
    }
}
