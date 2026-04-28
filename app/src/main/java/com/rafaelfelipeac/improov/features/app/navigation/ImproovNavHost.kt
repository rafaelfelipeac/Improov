package com.rafaelfelipeac.improov.features.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rafaelfelipeac.improov.features.app.screens.BackupRoute
import com.rafaelfelipeac.improov.features.app.screens.GoalDetailRoute
import com.rafaelfelipeac.improov.features.app.screens.GoalFormRoute
import com.rafaelfelipeac.improov.features.app.screens.GoalListRoute
import com.rafaelfelipeac.improov.features.app.screens.ProfileEditRoute
import com.rafaelfelipeac.improov.features.app.screens.ProfileRoute
import com.rafaelfelipeac.improov.features.app.screens.SettingsLanguageRoute
import com.rafaelfelipeac.improov.features.app.screens.SettingsRoute
import com.rafaelfelipeac.improov.features.app.screens.SplashRoute
import com.rafaelfelipeac.improov.features.app.screens.WelcomeRoute

@Composable
fun ImproovNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ImproovRoutes.SPLASH,
        modifier = modifier,
    ) {
        composable(ImproovRoutes.SPLASH) { SplashRoute(navController = navController) }
        composable(ImproovRoutes.WELCOME) { WelcomeRoute(navController = navController) }
        composable(ImproovRoutes.GOAL_LIST) { GoalListRoute(navController = navController) }
        composable(
            route = ImproovRoutes.GOAL_FORM,
            arguments =
                listOf(
                    navArgument(ImproovRoutes.GOAL_FORM_ARGUMENT) {
                        type = NavType.LongType
                        defaultValue = 0L
                    },
                ),
        ) { backStackEntry ->
            GoalFormRoute(
                navController = navController,
                goalId = backStackEntry.arguments?.getLong(ImproovRoutes.GOAL_FORM_ARGUMENT) ?: 0L,
            )
        }
        composable(
            route = ImproovRoutes.GOAL_DETAIL,
            arguments =
                listOf(
                    navArgument(ImproovRoutes.GOAL_ID_ARGUMENT) {
                        type = NavType.LongType
                    },
                ),
        ) { backStackEntry ->
            GoalDetailRoute(
                navController = navController,
                goalId = backStackEntry.arguments?.getLong(ImproovRoutes.GOAL_ID_ARGUMENT) ?: 0L,
            )
        }
        composable(ImproovRoutes.PROFILE) { ProfileRoute(navController = navController) }
        composable(ImproovRoutes.PROFILE_EDIT) { ProfileEditRoute(navController = navController) }
        composable(ImproovRoutes.SETTINGS) { SettingsRoute(navController = navController) }
        composable(ImproovRoutes.SETTINGS_LANGUAGE) { SettingsLanguageRoute(navController = navController) }
        composable(ImproovRoutes.BACKUP) { BackupRoute(navController = navController) }
    }
}
