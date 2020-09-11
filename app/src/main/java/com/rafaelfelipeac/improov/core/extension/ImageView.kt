package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import kotlin.math.ceil

fun ImageView.enableIcon(iconNormal: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconNormal)
}

fun ImageView.disableIcon(iconDark: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconDark)
}

fun ImageView.setWidthForProgress(goal: Goal, width: Int) {
    var px = if (goal.divideAndConquer) {
        val widthUnit = width / goal.goldValue
        ceil((goal.value * widthUnit)).toInt()
    } else {
        val widthUnit = width / goal.singleValue
        ceil((goal.value * widthUnit)).toInt()
    }

    if (px == 0) { // if px was 0, the width will be equivalent a match_constraint
        px = 1
    }
    if (px > width) { // if px was greater than width, need to limit
        px = width
    }

    val params = layoutParams
    params.width = px
    layoutParams = params
}
