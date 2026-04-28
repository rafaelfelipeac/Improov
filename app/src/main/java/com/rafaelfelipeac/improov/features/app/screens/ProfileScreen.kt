@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "NoUnusedImports",
    "MaximumLineLength",
    "MaxLineLength",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.BuildConfig
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes

@Composable
fun ProfileRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { profileViewModel() } ?: return
    val name by viewModel.name.collectAsStateCompat("")
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { viewModel.loadData() }
    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            navController.navigate(ImproovRoutes.WELCOME) {
                popUpTo(ImproovRoutes.PROFILE) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) { viewModel.generated.collect { snackbarMessage = context.getString(R.string.profile_data_created) } }
    LaunchedEffect(Unit) { viewModel.clean.collect { snackbarMessage = context.getString(R.string.profile_data_cleared) } }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    ProfileScreen(
        name = name,
        snackbarHostState = snackbarHostState,
        onAddGoal = { navController.navigate(ImproovRoutes.goalForm()) },
        onEditProfile = { navController.navigate(ImproovRoutes.PROFILE_EDIT) },
        onBackup = { navController.navigate(ImproovRoutes.BACKUP) },
        onSettings = { navController.navigate(ImproovRoutes.SETTINGS) },
        onShowWelcome = {
            viewModel.saveWelcome(false)
            viewModel.saveFirstTimeAdd(true)
            viewModel.saveFirstTimeList(false)
        },
        onGenerateData = { viewModel.generateData() },
        onClearData = { viewModel.clearData() },
    )
}

@Composable
private fun ProfileScreen(
    name: String,
    snackbarHostState: SnackbarHostState,
    onAddGoal: () -> Unit,
    onEditProfile: () -> Unit,
    onBackup: () -> Unit,
    onSettings: () -> Unit,
    onShowWelcome: () -> Unit,
    onGenerateData: () -> Unit,
    onClearData: () -> Unit,
) {
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { ScreenTopBar(title = stringResource(R.string.profile_title)) },
        floatingActionButton = {
            ElevatedButton(onClick = onAddGoal) {
                Text(text = stringResource(R.string.menu_add))
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = Dimens.ScreenHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
                ) {
                    Text(
                        text = if (name.isNotBlank()) name else stringResource(R.string.profile_add_name_message),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    TextButton(onClick = onEditProfile) {
                        Text(
                            text = if (name.isNotBlank()) {
                                stringResource(R.string.profile_edit_name_message)
                            } else {
                                stringResource(R.string.profile_add_name_message)
                            },
                        )
                    }
                }
            }

            ActionCard(title = stringResource(R.string.profile_show_welcome_message), onClick = onShowWelcome)
            ActionCard(title = stringResource(R.string.profile_button_backup), onClick = onBackup)
            ActionCard(title = stringResource(R.string.settings_title), onClick = onSettings)

            if (BuildConfig.DEBUG) {
                ActionCard(title = stringResource(R.string.profile_generate_data), onClick = onGenerateData)
                ActionCard(title = stringResource(R.string.profile_clear_data), onClick = onClearData)
            }
        }
    }
}
