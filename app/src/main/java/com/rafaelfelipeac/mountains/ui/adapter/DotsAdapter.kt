package com.rafaelfelipeac.mountains.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeOneFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeThreeFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeTwoFragment

class DotsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeOneFragment()
            1 -> WelcomeTwoFragment()
            2 -> WelcomeThreeFragment()
            else -> WelcomeOneFragment()
        }
    }
}