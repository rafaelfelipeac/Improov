package com.rafaelfelipeac.mountains.di

import com.rafaelfelipeac.mountains.ui.base.BaseActivity
import com.rafaelfelipeac.mountains.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.mountains.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.mountains.ui.fragments.stats.StatsFragment

interface Injector {

    fun inject(baseActivity: BaseActivity)

    fun inject(goalsFragment: GoalsFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(statsFragment: StatsFragment)
}
