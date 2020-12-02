package com.rafaelfelipeac.improov.core.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible

const val CL_HEIGHT = 350
const val CL_HEIGHT_2 = CL_HEIGHT - 100
const val CL_ANIMATION_DURATION: Int = 700

var animatorNavigation: ValueAnimator? = null
var animatorNavigationFake: ValueAnimator? = null

var showingNavigation = false
var hidingNavigation = false

fun CoordinatorLayout.show(fakeBottomNav: View) {
    if ((animatorNavigation?.isRunning == true) || hidingNavigation || isVisible) return

    animatorNavigation = ValueAnimator.ofInt(0, CL_HEIGHT)
    animatorNavigationFake = ValueAnimator.ofInt(0, CL_HEIGHT_2)

    animatorNavigation?.addUpdateListener { animation ->
        if (!hidingNavigation) {
            (layoutParams as ViewGroup.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    animatorNavigationFake?.addUpdateListener { animation ->
        if (!hidingNavigation) {
            (fakeBottomNav.layoutParams as ViewGroup.LayoutParams).height = (animation?.animatedValue as Int)
            fakeBottomNav.requestLayout()
        }
    }

    animatorNavigation?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            showingNavigation = true

            visible()
        }

        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)

            showingNavigation = false
        }
    })

    animatorNavigationFake?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            fakeBottomNav.visible()
        }
    })

    animatorNavigation?.duration = CL_ANIMATION_DURATION.toLong()
    animatorNavigation?.start()

    animatorNavigationFake?.duration = CL_ANIMATION_DURATION.toLong()
    animatorNavigationFake?.start()
}

fun CoordinatorLayout.hide(fakeBottomNav: View) {
    if ((animatorNavigation?.isRunning == true) || showingNavigation || !isVisible) return

    animatorNavigation = ValueAnimator.ofInt(CL_HEIGHT, 0)
    animatorNavigationFake = ValueAnimator.ofInt(CL_HEIGHT_2, 0)

    animatorNavigation?.addUpdateListener { animation ->
        if (!showingNavigation) {
            (layoutParams as ViewGroup.LayoutParams).height = (animation?.animatedValue as Int)
            requestLayout()
        }
    }

    animatorNavigationFake?.addUpdateListener { animation ->
        if (!showingNavigation) {
            (fakeBottomNav.layoutParams as ViewGroup.LayoutParams).height = (animation.animatedValue as Int)
            fakeBottomNav.requestLayout()
        }
    }

    animatorNavigation?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

            hidingNavigation = true
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)

            hidingNavigation = false

            gone()
        }
    })

    animatorNavigationFake?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)

            fakeBottomNav.gone()
        }
    })

    animatorNavigation?.duration = CL_ANIMATION_DURATION.toLong()
    animatorNavigation?.start()

    animatorNavigationFake?.duration = CL_ANIMATION_DURATION.toLong()
    animatorNavigationFake?.start()
}
