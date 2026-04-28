package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes

@Composable
fun SplashRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { splashViewModel() } ?: return
    val welcome by viewModel.welcome.collectAsStateCompat(false)

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(welcome) {
        if (welcome) {
            navController.navigate(ImproovRoutes.GOAL_LIST) {
                popUpTo(ImproovRoutes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(ImproovRoutes.WELCOME) {
                popUpTo(ImproovRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLg),
        ) {
            Text(
                text = stringResource(R.string.app_name_release),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
            )
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}
