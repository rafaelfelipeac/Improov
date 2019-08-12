package com.rafaelfelipeac.mountains.core.extension

import com.rafaelfelipeac.mountains.features.habit.PeriodType

fun PeriodType.getName(): String {
    return when (this) {
        PeriodType.PER_WEEK -> "semana"
        PeriodType.PER_MONTH -> "mês"
        PeriodType.PER_YEAR -> "ano"
        PeriodType.PER_CUSTOM -> "custom"
        PeriodType.PER_NONE -> "none"
    }
}
