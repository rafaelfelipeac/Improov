package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.features.commons.PeriodType

fun PeriodType.getName(context: Context): String {
    return when (this) {
        PeriodType.PER_WEEK -> context.getString(R.string.habit_form_period_type_week)
        PeriodType.PER_MONTH -> context.getString(R.string.habit_form_period_type_month)
        PeriodType.PER_YEAR -> context.getString(R.string.habit_form_period_type_year)
        PeriodType.PER_CUSTOM -> context.getString(R.string.habit_form_period_type_custom)
        PeriodType.PER_NONE -> context.getString(R.string.habit_form_period_type_none)
    }
}
