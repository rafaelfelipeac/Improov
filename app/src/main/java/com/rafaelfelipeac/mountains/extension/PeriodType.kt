package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.PeriodType

fun PeriodType.getName(): String {
    return when (this) {
        PeriodType.PER_WEEK -> "semana"
        PeriodType.PER_MONTH -> "mês"
        PeriodType.PER_YEAR -> "ano"
        PeriodType.PER_CUSTOM -> "custom"
        PeriodType.PER_NONE -> "none"
    }
}
