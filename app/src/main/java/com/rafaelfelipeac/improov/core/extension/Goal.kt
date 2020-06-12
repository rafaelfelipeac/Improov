package com.rafaelfelipeac.improov.core.extension

import com.rafaelfelipeac.improov.features.goal.domain.model.Goal

const val PERCENTAGE_MAX = 100

fun Goal.getPercentage() = if (divideAndConquer) {
    (value / goldValue) * PERCENTAGE_MAX
} else {
    (value / singleValue) * PERCENTAGE_MAX
}
