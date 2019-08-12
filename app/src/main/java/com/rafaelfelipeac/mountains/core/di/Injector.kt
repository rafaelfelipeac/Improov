package com.rafaelfelipeac.mountains.core.di

import com.rafaelfelipeac.mountains.core.platform.BaseActivity
import com.rafaelfelipeac.mountains.features.createUser.CreateUserFragment
import com.rafaelfelipeac.mountains.features.editProfile.EditProfileFragment
import com.rafaelfelipeac.mountains.features.forgotPassword.ForgotPasswordFragment
import com.rafaelfelipeac.mountains.features.goal.presentation.GoalFragment
import com.rafaelfelipeac.mountains.features.goal.presentation.GoalFormFragment
import com.rafaelfelipeac.mountains.features.habit.presentation.HabitFragment
import com.rafaelfelipeac.mountains.features.habit.presentation.HabitFormFragment
import com.rafaelfelipeac.mountains.features.list.ListFragment
import com.rafaelfelipeac.mountains.features.login.LoginFragment
import com.rafaelfelipeac.mountains.features.profile.ProfileFragment
import com.rafaelfelipeac.mountains.features.stats.StatsFragment
import com.rafaelfelipeac.mountains.features.today.TodayFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeOneFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeThreeFragment
import com.rafaelfelipeac.mountains.features.welcome.WelcomeTwoFragment

interface Injector {
    fun inject(baseActivity: BaseActivity)

    fun inject(listFragment: ListFragment)

    fun inject(goalFragment: GoalFragment)

    fun inject(statsFragment: StatsFragment)

    fun inject(createUserFragment: CreateUserFragment)

    fun inject(editProfileFragment: EditProfileFragment)

    fun inject(forgotPasswordFragment: ForgotPasswordFragment)

    fun inject(goalFormFragment: GoalFormFragment)

    fun inject(habitFragment: HabitFragment)

    fun inject(habitFormFragment: HabitFormFragment)

    fun inject(loginFragment: LoginFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(todayFragment: TodayFragment)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(welcomeOneFragment: WelcomeOneFragment)

    fun inject(welcomeTwoFragment: WelcomeTwoFragment)

    fun inject(welcomeThreeFragment: WelcomeThreeFragment)
}
