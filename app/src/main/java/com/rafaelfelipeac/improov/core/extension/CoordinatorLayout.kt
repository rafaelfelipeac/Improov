package com.rafaelfelipeac.improov.core.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible

const val CL_ANIMATION_DURATION: Int = 700
const val HEIGHT_MULTIPLIER = 0.66
const val MINIMUM_HEIGHT = 1

var animatorNavigation: ValueAnimator? = null
var animatorNavigationFake: ValueAnimator? = null

var showingNavigation = false
var hidingNavigation = false

var navigationHeight = 0
var fakeNavigationHeight = 0

fun CoordinatorLayout.show(fakeBottomNav: View) {
    if ((animatorNavigation?.isRunning == true) || hidingNavigation || isVisible) return

    if (navigationHeight == 0 && fakeNavigationHeight == 0 && height > 0) {
        navigationHeight = height
        fakeNavigationHeight = (height * HEIGHT_MULTIPLIER).toInt()
    }

    animatorNavigation = ValueAnimator.ofInt(0, navigationHeight)
    animatorNavigationFake = ValueAnimator.ofInt(0, fakeNavigationHeight)

    animatorNavigation?.addUpdateListener { animation ->
        if (!hidingNavigation) {
            if (height < navigationHeight) {
                visible()
            }

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

            if (height == MINIMUM_HEIGHT) {
                (layoutParams as ViewGroup.LayoutParams).height = navigationHeight
                requestLayout()
            }

            invisible()
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

    if (navigationHeight == 0 && fakeNavigationHeight == 0 && height > 0) {
        navigationHeight = height
        fakeNavigationHeight = (height * HEIGHT_MULTIPLIER).toInt()
    }

    animatorNavigation = ValueAnimator.ofInt(navigationHeight, 0)
    animatorNavigationFake = ValueAnimator.ofInt(fakeNavigationHeight, 0)

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

            invisible()
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
