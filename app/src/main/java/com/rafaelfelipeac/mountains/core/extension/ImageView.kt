package com.rafaelfelipeac.mountains.core.extension

import android.content.Context
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.mountains.core.platform.BaseFragment
import com.rafaelfelipeac.mountains.features.goal.Goal
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

    val px = if (goal.divideAndConquer) {
        ceil(((goal.value * 100) / goal.goldValue) * logicalDensity).toInt()
    } else {
        ceil(((goal.value * 100) / goal.singleValue) * logicalDensity).toInt()
    }

    val lp = layoutParams as ConstraintLayout.LayoutParams
    lp.width = px
    layoutParams = lp
}