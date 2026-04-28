package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.LocaleHelper
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.findActivity
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel

@Composable
fun SettingsLanguageRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { settingsLanguageViewModel() } ?: return
    val language by viewModel.language.collectAsStateCompat("")
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.loadData() }
    LaunchedEffect(language) {
        if (language.isNotBlank()) {
            LocaleHelper.setLocale(context, language)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            context.findActivity()?.recreate()
            navController.navigateUp()
        }
    }

    SettingsLanguageScreen(
        currentLanguage = language,
        snackbarHostState = snackbarHostState,
        onBack = { navController.navigateUp() },
        onLanguageSelected = { viewModel.saveLanguage(it) },
    )
}

@Composable
private fun SettingsLanguageScreen(
    currentLanguage: String,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.settings_language_language_title),
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
            val portugueseKey = context.getString(R.string.settings_language_key_portuguese)
            val englishKey = context.getString(R.string.settings_language_key_english)
            LanguageRow(
                selected = currentLanguage == portugueseKey,
                title = stringResource(R.string.settings_language_radio_portuguese),
                onClick = { onLanguageSelected(portugueseKey) },
            )
            LanguageRow(
                selected = currentLanguage == englishKey,
                title = stringResource(R.string.settings_language_radio_english),
                onClick = { onLanguageSelected(englishKey) },
            )
        }
    }
}

@Composable
private fun LanguageRow(selected: Boolean, title: String, onClick: () -> Unit) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.ScreenHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(modifier = Modifier.padding(start = Dimens.SpacingSm))
            Text(text = title)
        }
    }
}
