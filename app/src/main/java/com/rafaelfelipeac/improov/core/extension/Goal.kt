package com.rafaelfelipeac.improov.core.extension

import com.rafaelfelipeac.improov.features.goal.domain.model.Goal

fun Goal.getPercentage() = if (divideAndConquer) {
    (value / goldValue) * 100
} else {
    (value / singleValue) * 100
}
