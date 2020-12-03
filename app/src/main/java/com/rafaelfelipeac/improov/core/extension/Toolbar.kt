package com.rafaelfelipeac.improov.core.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

const val TOOLBAR_ANIMATION_DURATION: Int = 700

var animatorToolbar: ValueAnimator? = null

var hidingToolbar = false
var showingToolbar = false

var toolbarHeight = 0

fun Toolbar.show(supportActionBar: ActionBar?) {
    if ((animatorToolbar?.isRunning == true) || hidingToolbar || (supportActionBar?.isShowing != false))
        return

    if (toolbarHeight == 0 && height > 0) {
        toolbarHeight = height
    }

    animatorToolbar = ValueAnimator.ofInt(0, toolbarHeight)

    animatorToolbar?.addUpdateListener { animation ->
        if (!hidingToolbar) {
            (layoutParams as AppBarLayout.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    animatorToolbar?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            showingToolbar = true

            supportActionBar.show()
        }

        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)

            showingToolbar = false
        }
    })

    animatorToolbar?.duration = TOOLBAR_ANIMATION_DURATION.toLong()
    animatorToolbar?.start()
}

fun Toolbar.hide(supportActionBar: ActionBar?) {
    if ((animatorToolbar?.isRunning == true) || showingToolbar || (supportActionBar?.isShowing != true))
        return

    if (toolbarHeight == 0 && height > 0) {
        toolbarHeight = height
    }

    animatorToolbar = ValueAnimator.ofInt(toolbarHeight, 0)

    animatorToolbar?.addUpdateListener { animation ->
        if (!showingToolbar) {
            (layoutParams as AppBarLayout.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    animatorToolbar?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            hidingToolbar = true
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)

            hidingToolbar = false

            supportActionBar.hide()
        }
    })

    animatorToolbar?.duration = TOOLBAR_ANIMATION_DURATION.toLong()
    animatorToolbar?.start()
}
