package com.rafaelfelipeac.improov.core.di

import com.rafaelfelipeac.improov.core.platform.base.BaseActivity
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profilename.ProfileNameFragment
import com.rafaelfelipeac.improov.features.settings.presentation.SettingsLanguageFragment
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeFragment
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeItemFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(welcomeItemFragment: WelcomeItemFragment)

    fun inject(goalListFragment: GoalListFragment)

    fun inject(goalFormFragment: GoalFormFragment)

    fun inject(goalDetailFragment: GoalDetailFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(profileNameFragment: ProfileNameFragment)

    fun inject(settingsLanguageFragment: SettingsLanguageFragment)
}
