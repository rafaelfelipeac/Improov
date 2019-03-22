package com.rafaelfelipeac.mountains.extension

import com.rafaelfelipeac.mountains.models.Goal

fun Goal.getPercentage() = if (trophies) { (value / goldValue) * 100 } else {(value / medalValue) * 100 }