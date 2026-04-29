package com.rafaelfelipeac.improov.features.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.core.ui.theme.ImproovTheme
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes
import com.rafaelfelipeac.improov.features.app.navigation.ImproovTopLevelDestination

@Composable
fun ImproovAppShell(
    navController: NavHostController,
    themeMode: AppThemeMode,
    content: @Composable (PaddingValues) -> Unit,
) {
    ImproovTheme(themeMode = themeMode) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val route = backStackEntry?.destination?.route
        val selectedDestination = topLevelDestinationFor(route)
        val showChrome = shouldShowBottomBar(route)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showChrome) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedDestination == ImproovTopLevelDestination.GOALS,
                            onClick = {
                                navController.navigate(ImproovRoutes.GOAL_LIST) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(ImproovRoutes.GOAL_LIST) { inclusive = false }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_list),
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(R.string.navigation_goals)) },
                        )
                        NavigationBarItem(
                            selected = selectedDestination == ImproovTopLevelDestination.PROFILE,
                            onClick = {
                                navController.navigate(ImproovRoutes.PROFILE) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(ImproovRoutes.PROFILE) { inclusive = false }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_profile),
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(R.string.navigation_profile)) },
                        )
                    }
                }
            },
            floatingActionButton = {
                if (route == ImproovRoutes.GOAL_LIST) {
                    FloatingActionButton(
                        onClick = { navController.navigate(ImproovRoutes.goalForm()) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = null,
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { innerPadding ->
            Surface(modifier = Modifier.fillMaxSize()) {
                content(innerPadding)
            }
        }
    }
}

private fun topLevelDestinationFor(route: String?): ImproovTopLevelDestination =
    when {
        route == ImproovRoutes.PROFILE ||
            route == ImproovRoutes.PROFILE_EDIT ||
            route == ImproovRoutes.SETTINGS ||
            route == ImproovRoutes.SETTINGS_LANGUAGE ||
            route == ImproovRoutes.BACKUP -> ImproovTopLevelDestination.PROFILE

        route == ImproovRoutes.GOAL_LIST ||
            route?.startsWith("goals/") == true ||
            route?.startsWith("goal-form/") == true -> ImproovTopLevelDestination.GOALS

        else -> ImproovTopLevelDestination.GOALS
    }

private fun shouldShowBottomBar(route: String?): Boolean =
    route != ImproovRoutes.SPLASH && route != ImproovRoutes.WELCOME
