package com.rafaelfelipeac.improov.core.di.provider

import com.rafaelfelipeac.improov.features.backup.presentation.BackupViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profileedit.ProfileEditViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settings.SettingsViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage.SettingsLanguageViewModel
import com.rafaelfelipeac.improov.features.splash.presentation.SplashViewModel
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeViewModel
import com.rafaelfelipeac.improov.future.habit.presentation.HabitFormViewModel
import com.rafaelfelipeac.improov.future.stats.presentation.StatsViewModel
import com.rafaelfelipeac.improov.future.today.presentation.TodayViewModel

interface ViewModelProvider {

    fun splashViewModel(): SplashViewModel
    fun welcomeViewModel(): WelcomeViewModel
    fun goalListViewModel(): GoalListViewModel
    fun goalFormViewModel(): GoalFormViewModel
    fun goalDetailViewModel(): GoalDetailViewModel
    fun profileViewModel(): ProfileViewModel
    fun profileEditViewModel(): ProfileEditViewModel
    fun settingsViewModel(): SettingsViewModel
    fun settingsLanguageViewModel(): SettingsLanguageViewModel
    fun backupViewModel(): BackupViewModel
    fun habitViewModel(): HabitFormViewModel
    fun statsViewModel(): StatsViewModel
    fun todayViewModel(): TodayViewModel
}
