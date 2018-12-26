package com.rafaelfelipeac.readmore.di

import com.rafaelfelipeac.readmore.ui.base.BaseActivity
import com.rafaelfelipeac.readmore.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.readmore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.readmore.ui.fragments.home.HomeFragment
import com.rafaelfelipeac.readmore.ui.fragments.metrics.MetricsFragment
import com.rafaelfelipeac.readmore.ui.fragments.search.SearchFragment
import com.rafaelfelipeac.readmore.ui.fragments.settings.SettingsFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(goalsFragment: GoalsFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(metricsFragment: MetricsFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(settingsFragment: SettingsFragment)
}
