package com.rafaelfelipeac.improov.features.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
        composable(ImproovRoutes.SPLASH) { EmptyRoute() }
        composable(ImproovRoutes.WELCOME) { EmptyRoute() }
        composable(ImproovRoutes.GOAL_LIST) { EmptyRoute() }
        composable(ImproovRoutes.GOAL_FORM) { EmptyRoute() }
        composable(
            route = ImproovRoutes.GOAL_DETAIL,
            arguments =
                listOf(
                    navArgument(ImproovRoutes.GOAL_ID_ARGUMENT) {
                        type = NavType.LongType
                    },
                ),
        ) {
            EmptyRoute()
        }
        composable(ImproovRoutes.PROFILE) { EmptyRoute() }
        composable(ImproovRoutes.PROFILE_EDIT) { EmptyRoute() }
        composable(ImproovRoutes.SETTINGS) { EmptyRoute() }
        composable(ImproovRoutes.SETTINGS_LANGUAGE) { EmptyRoute() }
        composable(ImproovRoutes.BACKUP) { EmptyRoute() }
    }
}

@Composable
private fun EmptyRoute() {
    Box(modifier = Modifier.fillMaxSize())
}
