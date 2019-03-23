package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.Goal

fun Goal.getPercentage() = if (mountains) { (value / goldValue) * 100 } else {(value / singleValue) * 100 }