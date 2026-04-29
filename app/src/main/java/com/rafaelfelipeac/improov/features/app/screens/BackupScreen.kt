@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "NoUnusedImports",
    "MaximumLineLength",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.formatToDate
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import java.util.Date

@Composable
fun BackupRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { backupViewModel() } ?: return
    val exportDate by viewModel.exportDate.collectAsStateCompat(0L)
    val importDate by viewModel.importDate.collectAsStateCompat(0L)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var pendingPermissionAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val writePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                pendingPermissionAction?.invoke()
            } else {
                showPermissionDialog = true
            }
        }
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val text = readTextFromUri(context, uri)
                if (text.isNotBlank()) {
                    viewModel.importDatabase(text)
                } else {
                    snackbarMessage = context.getString(R.string.backup_import_error)
                }
            }
        }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    LaunchedEffect(Unit) { viewModel.loadData() }
    LaunchedEffect(Unit) {
        viewModel.export.collect { json ->
            if (json.isNotBlank()) {
                viewModel.getExportDate()
                val file = saveBackupFile(context, json)
                shareFile(context, file)
                snackbarMessage = context.getString(R.string.backup_export_success)
            } else {
                snackbarMessage = context.getString(R.string.backup_export_error)
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.import.collect { success ->
            if (success) {
                viewModel.getImportDate()
                snackbarMessage = context.getString(R.string.backup_import_success)
            } else {
                snackbarMessage = context.getString(R.string.backup_import_error)
            }
        }
    }

    BackupScreen(
        exportDate = exportDate,
        importDate = importDate,
        snackbarHostState = snackbarHostState,
        onBack = { navController.navigateUp() },
        onExport = {
            val action = { viewModel.exportDatabase() }
            pendingPermissionAction = action
            if (hasStoragePermissions(context)) {
                action()
            } else {
                writePermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ),
                )
            }
        },
        onImport = {
            val action = { fileLauncher.launch(arrayOf("*/*")) }
            pendingPermissionAction = action
            if (hasStoragePermissions(context)) {
                action()
            } else {
                writePermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ),
                )
            }
        },
    )

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(text = stringResource(R.string.backup_title)) },
            text = { Text(text = stringResource(R.string.backup_permission_storage_settings_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        openAppSettings(context)
                    },
                ) {
                    Text(text = stringResource(R.string.backup_permission_storage_settings_positive))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(text = stringResource(R.string.backup_permission_storage_settings_negative))
                }
            },
        )
    }
}

@Composable
private fun BackupScreen(
    exportDate: Long,
    importDate: Long,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
) {
    val context = LocalContext.current
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.backup_title),
                navigation = {
                    BackNavigationButton(onClick = onBack)
                },
            )
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
            Text(
                text = stringResource(R.string.backup_message),
                style = MaterialTheme.typography.bodyLarge,
            )
            ActionCard(
                title = stringResource(R.string.backup_button_export),
                subtitle =
                    if (exportDate > 0) {
                        stringResource(R.string.backup_date_export, Date(exportDate).formatToDate(context))
                    } else {
                        stringResource(R.string.backup_button_export)
                    },
                onClick = onExport,
            )
            ActionCard(
                title = stringResource(R.string.backup_button_import),
                subtitle =
                    if (importDate > 0) {
                        stringResource(R.string.backup_date_import, Date(importDate).formatToDate(context))
                    } else {
                        stringResource(R.string.backup_button_import)
                    },
                onClick = onImport,
            )
        }
    }
}
