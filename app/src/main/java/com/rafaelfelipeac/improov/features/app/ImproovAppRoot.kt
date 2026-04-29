package com.rafaelfelipeac.improov.features.app

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovNavHost

@Composable
fun ImproovAppRoot() {
    val navController = rememberNavController()
    val settingsViewModel = rememberAppViewModel { settingsViewModel() } ?: return
    val themeMode by settingsViewModel.themeMode.collectAsStateCompat(AppThemeMode.LIGHT)

    ImproovAppShell(
        navController = navController,
        themeMode = themeMode,
    ) { padding ->
        ImproovNavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}
