package com.rafaelfelipeac.domore.extension

import com.rafaelfelipeac.domore.models.Goal

fun Goal.getPercentage() = if (trophies) { (value / goldValue) * 100 } else {(value / medalValue) * 100 }