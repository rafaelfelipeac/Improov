package com.rafaelfelipeac.mountains.core.extension

import com.rafaelfelipeac.mountains.features.commons.Goal

fun Goal.getPercentage() = if (divideAndConquer) {
    (value / goldValue) * 100
} else {
    (value / singleValue) * 100
}