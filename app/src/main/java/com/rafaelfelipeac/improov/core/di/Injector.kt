package com.rafaelfelipeac.improov.core.di

import com.rafaelfelipeac.improov.core.platform.base.BaseActivity
import com.rafaelfelipeac.improov.features.goal.presentation.goal.GoalFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormFragment
import com.rafaelfelipeac.improov.features.habit.HabitFormFragment
import com.rafaelfelipeac.improov.features.habit.HabitFragment
import com.rafaelfelipeac.improov.features.list.ListFragment
import com.rafaelfelipeac.improov.features.profile.ProfileFragment
import com.rafaelfelipeac.improov.features.profilename.ProfileNameFragment
import com.rafaelfelipeac.improov.features.settings.SettingsLanguageFragment
import com.rafaelfelipeac.improov.features.stats.StatsFragment
import com.rafaelfelipeac.improov.features.today.presentation.TodayFragment
import com.rafaelfelipeac.improov.features.welcome.WelcomeItemFragment
import com.rafaelfelipeac.improov.features.welcome.WelcomeFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(listFragment: ListFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(statsFragment: StatsFragment)

    fun inject(profileNameFragment: ProfileNameFragment)

    fun inject(goalFormFragment: GoalFormFragment)

    fun inject(habitFragment: HabitFragment)

    fun inject(habitFormFragment: HabitFormFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(todayFragment: TodayFragment)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(welcomeItemFragment: WelcomeItemFragment)

    fun inject (settingsLanguageFragment: SettingsLanguageFragment)
}
