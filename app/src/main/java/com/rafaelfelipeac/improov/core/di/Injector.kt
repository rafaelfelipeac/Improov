package com.rafaelfelipeac.improov.core.di

import com.rafaelfelipeac.improov.core.platform.base.BaseActivity
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListFragment
import com.rafaelfelipeac.improov.features.profile.ProfileFragment
import com.rafaelfelipeac.improov.features.profilename.ProfileNameFragment
import com.rafaelfelipeac.improov.features.settings.SettingsLanguageFragment
import com.rafaelfelipeac.improov.features.welcome.WelcomeFragment
import com.rafaelfelipeac.improov.features.welcome.WelcomeItemFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(goalListFragment: GoalListFragment)

    fun inject(goalDetailFragment: GoalDetailFragment)

    fun inject(goalFormFragment: GoalFormFragment)

    fun inject(profileNameFragment: ProfileNameFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(welcomeItemFragment: WelcomeItemFragment)

    fun inject (settingsLanguageFragment: SettingsLanguageFragment)
}
