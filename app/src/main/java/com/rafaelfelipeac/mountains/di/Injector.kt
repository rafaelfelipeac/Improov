package com.rafaelfelipeac.mountains.di

import com.rafaelfelipeac.mountains.ui.base.BaseActivity
import com.rafaelfelipeac.mountains.ui.fragments.createUser.CreateUserFragment
import com.rafaelfelipeac.mountains.ui.fragments.editProfile.EditProfileFragment
import com.rafaelfelipeac.mountains.ui.fragments.forgotPassword.ForgotPasswordFragment
import com.rafaelfelipeac.mountains.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.mountains.ui.fragments.goalForm.GoalFormFragment
import com.rafaelfelipeac.mountains.ui.fragments.habit.HabitFragment
import com.rafaelfelipeac.mountains.ui.fragments.habitForm.HabitFormFragment
import com.rafaelfelipeac.mountains.ui.fragments.list.ListFragment
import com.rafaelfelipeac.mountains.ui.fragments.login.LoginFragment
import com.rafaelfelipeac.mountains.ui.fragments.profile.ProfileFragment
import com.rafaelfelipeac.mountains.ui.fragments.stats.StatsFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeOneFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeThreeFragment
import com.rafaelfelipeac.mountains.ui.fragments.welcome.WelcomeTwoFragment

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
