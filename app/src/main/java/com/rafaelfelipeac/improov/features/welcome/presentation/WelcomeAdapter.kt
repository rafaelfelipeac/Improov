package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WelcomeAdapter(val fragment: WelcomeFragment, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeItemFragment(
                fragment,
                WelcomePosition.FIRST
            )
            1 -> WelcomeItemFragment(
                fragment,
                WelcomePosition.SECOND
            )
            2 -> WelcomeItemFragment(
                fragment,
                WelcomePosition.THIRD
            )
            else -> WelcomeItemFragment(
                fragment,
                WelcomePosition.NONE
            )
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}
