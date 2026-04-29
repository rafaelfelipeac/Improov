package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel

@Composable
fun ProfileEditRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { profileEditViewModel() } ?: return
    val name by viewModel.name.collectAsStateCompat("")
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var value by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.loadData() }
    LaunchedEffect(name) { value = name }
    LaunchedEffect(Unit) { viewModel.saved.collect { navController.navigateUp() } }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    ProfileEditScreen(
        value = value,
        snackbarHostState = snackbarHostState,
        onValueChange = { value = it },
        onSave = {
            if (value.isBlank()) {
                snackbarMessage = context.getString(R.string.profile_edit_empty_fields)
            } else {
                viewModel.saveName(value)
            }
        },
        onBack = { navController.navigateUp() },
    )
}

@Composable
private fun ProfileEditScreen(
    value: String,
    snackbarHostState: SnackbarHostState,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.profile_edit_title),
                navigation = {
                    BackNavigationButton(onClick = onBack)
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text(text = stringResource(R.string.profile_edit_save_message))
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
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.profile_edit_name_hint)) },
                singleLine = true,
            )
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.profile_edit_save_message))
            }
        }
    }
}
