package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.BuildConfig
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes

@Composable
fun SettingsRoute(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = rememberAppViewModel { settingsViewModel() } ?: return
    val themeMode by viewModel.themeMode.collectAsStateCompat(AppThemeMode.LIGHT)

    SettingsScreen(
        themeMode = themeMode,
        onBack = { navController.navigateUp() },
        onLanguage = { navController.navigate(ImproovRoutes.SETTINGS_LANGUAGE) },
        onRate = { openMarketPage(context) },
        onContact = { openFeedbackMail(context) },
        onThemeChange = { viewModel.saveThemeMode(it) },
    )
}

@Composable
private fun SettingsScreen(
    themeMode: AppThemeMode,
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    onRate: () -> Unit,
    onContact: () -> Unit,
    onThemeChange: (AppThemeMode) -> Unit,
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
            ThemeSettingCard(
                themeMode = themeMode,
                onThemeChange = onThemeChange,
            )
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

@Composable
private fun ThemeSettingCard(
    themeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
        ) {
            Text(
                text = stringResource(R.string.settings_theme_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text =
                    if (themeMode == AppThemeMode.DARK) {
                        stringResource(R.string.settings_theme_dark)
                    } else {
                        stringResource(R.string.settings_theme_light)
                    },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Switch(
                checked = themeMode == AppThemeMode.DARK,
                onCheckedChange = { isDark ->
                    onThemeChange(if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT)
                },
            )
        }
    }
}
