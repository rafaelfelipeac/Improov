package com.rafaelfelipeac.readmore.di

import com.rafaelfelipeac.readmore.ui.base.BaseActivity
import com.rafaelfelipeac.readmore.ui.fragments.*
import com.rafaelfelipeac.readmore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.readmore.ui.fragments.home.HomeFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(goalsFragment: GoalsFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(metricasFragment: MetricasFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(settingsFragment: SettingsFragment)
}
