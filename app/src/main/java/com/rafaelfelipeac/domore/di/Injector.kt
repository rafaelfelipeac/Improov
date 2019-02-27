package com.rafaelfelipeac.domore.di

import com.rafaelfelipeac.domore.ui.base.BaseActivity
import com.rafaelfelipeac.domore.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.domore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.domore.ui.fragments.metrics.MetricsFragment
import com.rafaelfelipeac.domore.ui.fragments.search.SearchFragment
import com.rafaelfelipeac.domore.ui.fragments.settings.SettingsFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(goalsFragment: GoalsFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(metricsFragment: MetricsFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(settingsFragment: SettingsFragment)
}
