package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.Goal
import kotlin.math.ceil

fun ImageView.enableIcon(iconNormal: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconNormal)
}

fun ImageView.disableIcon(iconDark: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconDark)
}

fun ImageView.setWidthForProgress(goal: Goal, fragment: BaseFragment) {
    val metrics = DisplayMetrics()
    fragment.activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
    val logicalDensity = metrics.density

    val width = (200 * logicalDensity).toInt()

    var px = if (goal.divideAndConquer) {
        ceil(((goal.value * width) / goal.goldValue)).toInt()
    } else {
        ceil(((goal.value * width) / goal.singleValue)).toInt()
    }

    val lp = layoutParams as ConstraintLayout.LayoutParams

    if (px == 0) { // if px was 0, the width will be equivalent a match_constraint
        px = 1
    }
    if (px > width) { // if px was greater than width, need to limit
        px = width
    }

    lp.width = px
    layoutParams = lp
}
