package com.rafaelfelipeac.mountains.features.welcome

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WelcomeAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeAFragment()
            1 -> WelcomeBFragment()
            2 -> WelcomeCFragment()
            else -> WelcomeAFragment()
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}