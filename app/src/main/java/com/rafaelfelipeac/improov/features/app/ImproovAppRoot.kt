package com.rafaelfelipeac.improov.features.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.rafaelfelipeac.improov.features.app.navigation.ImproovNavHost

@Composable
fun ImproovAppRoot() {
    val navController = rememberNavController()

    ImproovAppShell {
        ImproovNavHost(navController = navController)
    }
}
