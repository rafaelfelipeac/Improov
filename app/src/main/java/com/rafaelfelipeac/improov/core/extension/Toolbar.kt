package com.rafaelfelipeac.improov.core.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

const val TOOLBAR_HEIGHT = 224
const val TOOLBAR_ANIMATION_DURATION: Int = 2000

var valueAnimator: ValueAnimator? = null

var hiden = false
var showing = false

fun Toolbar.hide(supportActionBar: ActionBar?) {
    if ((valueAnimator?.isRunning == true) || showing || !needToHidden(
            supportActionBar
        )
    ) {
        return
    }

    valueAnimator = ValueAnimator.ofInt(TOOLBAR_HEIGHT, 0)

    valueAnimator?.addUpdateListener { animation ->
        if (!showing) {
            (layoutParams as AppBarLayout.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    valueAnimator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            hiden = true
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)

            hiden = false

            supportActionBar?.hide()
        }
    })

    valueAnimator?.duration = TOOLBAR_ANIMATION_DURATION.toLong()
    valueAnimator?.start()
}

fun Toolbar.show(supportActionBar: ActionBar?) {
    if ((valueAnimator?.isRunning == true) || hiden || !needToShow(
            supportActionBar
        )
    ) {
        return
    }
    valueAnimator = ValueAnimator.ofInt(0, TOOLBAR_HEIGHT)

    valueAnimator?.addUpdateListener { animation ->
        if (!hiden) {
            (layoutParams as AppBarLayout.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    valueAnimator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            showing = true

            supportActionBar?.show()
        }

        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)

            showing = false
        }
    })

    valueAnimator?.duration = TOOLBAR_ANIMATION_DURATION.toLong()
    valueAnimator?.start()
}

private fun needToHidden(supportActionBar: ActionBar?): Boolean {
    return supportActionBar?.isShowing == true
}

private fun needToShow(supportActionBar: ActionBar?): Boolean {
    return supportActionBar?.isShowing == false
}
