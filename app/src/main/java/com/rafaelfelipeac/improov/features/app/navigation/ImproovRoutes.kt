package com.rafaelfelipeac.improov.features.app.navigation

object ImproovRoutes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val GOAL_LIST = "goals"
    const val GOAL_FORM = "goal-form"
    const val GOAL_ID_ARGUMENT = "goalId"
    const val GOAL_DETAIL = "goals/{$GOAL_ID_ARGUMENT}"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile/edit"
    const val SETTINGS = "settings"
    const val SETTINGS_LANGUAGE = "settings/language"
    const val BACKUP = "backup"

    fun goalDetail(goalId: Long): String = "goals/$goalId"
}
