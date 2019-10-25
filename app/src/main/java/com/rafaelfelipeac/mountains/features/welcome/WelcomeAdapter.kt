package com.rafaelfelipeac.mountains.features.welcome

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WelcomeAdapter(val fragment: WelcomeFragment, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeItemFragment(fragment, 0)
            1 -> WelcomeItemFragment(fragment, 1)
            2 -> WelcomeItemFragment(fragment, 2)
            else -> WelcomeItemFragment(fragment, 0)
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}