package com.rafaelfelipeac.mountains.features.welcome

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WelcomeDotsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeOneFragment()
            1 -> WelcomeTwoFragment()
            2 -> WelcomeThreeFragment()
            else -> WelcomeOneFragment()
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}