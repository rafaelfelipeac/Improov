package com.rafaelfelipeac.mountains.core.di

import com.rafaelfelipeac.mountains.core.platform.base.BaseActivity
import com.rafaelfelipeac.mountains.features.createuser.CreateUserFragment
import com.rafaelfelipeac.mountains.features.profileedit.ProfileEditFragment
import com.rafaelfelipeac.mountains.features.forgotpassword.ForgotPasswordFragment
import com.rafaelfelipeac.mountains.features.goal.presentation.goal.GoalFragment
import com.rafaelfelipeac.mountains.features.goal.presentation.goalform.GoalFormFragment
import com.rafaelfelipeac.mountains.features.habit.HabitFragment
import com.rafaelfelipeac.mountains.features.habit.HabitFormFragment
import com.rafaelfelipeac.mountains.features.list.ListFragment
import com.rafaelfelipeac.mountains.features.login.LoginFragment
import com.rafaelfelipeac.mountains.features.profile.ProfileFragment
import com.rafaelfelipeac.mountains.features.settings.SettingsLanguageFragment
import com.rafaelfelipeac.mountains.features.stats.StatsFragment
import com.rafaelfelipeac.mountains.features.today.presentation.TodayFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeAFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeCFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeBFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(listFragment: ListFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(statsFragment: StatsFragment)

    fun inject(createUserFragment: CreateUserFragment)

    fun inject(profileEditFragment: ProfileEditFragment)

    fun inject(forgotPasswordFragment: ForgotPasswordFragment)

    fun inject(goalFormFragment: GoalFormFragment)

    fun inject(habitFragment: HabitFragment)

    fun inject(habitFormFragment: HabitFormFragment)

    fun inject(loginFragment: LoginFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(todayFragment: TodayFragment)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(welcomeAFragment: WelcomeAFragment)

    fun inject(welcomeBFragment: WelcomeBFragment)

    fun inject(welcomeCFragment: WelcomeCFragment)

    fun inject (settingsLanguageFragment: SettingsLanguageFragment)
}
