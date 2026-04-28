package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.BuildConfig
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes

@Composable
fun SettingsRoute(navController: NavHostController) {
    val context = LocalContext.current
    SettingsScreen(
        onBack = { navController.navigateUp() },
        onLanguage = { navController.navigate(ImproovRoutes.SETTINGS_LANGUAGE) },
        onRate = { openMarketPage(context) },
        onContact = { openFeedbackMail(context) },
    )
}

@Composable
private fun SettingsScreen(
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    onRate: () -> Unit,
    onContact: () -> Unit,
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.settings_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            ActionCard(
                title = stringResource(R.string.settings_language_title),
                subtitle = stringResource(R.string.settings_language_message),
                onClick = onLanguage,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_rate_title),
                subtitle = stringResource(R.string.settings_about_rate_summary),
                onClick = onRate,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_contact_title),
                subtitle = stringResource(R.string.settings_about_contact_summary),
                onClick = onContact,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_version_title),
                subtitle = BuildConfig.VERSION_NAME,
                onClick = {},
            )
        }
    }
}
