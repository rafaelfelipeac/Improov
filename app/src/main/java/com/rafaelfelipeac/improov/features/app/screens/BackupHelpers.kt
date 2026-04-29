@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import com.rafaelfelipeac.improov.R
import java.io.File

internal fun openMarketPage(context: Context) {
    val appPackageName = context.packageName
    val marketUri = Uri.parse("market://details?id=$appPackageName")
    val playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")

    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (_: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, playStoreUri))
    }
}

internal fun openFeedbackMail(context: Context) {
    val emailIntent =
        Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", context.getString(R.string.settings_email_to), null),
        ).apply {
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title))
        }

    context.startActivity(
        Intent.createChooser(
            emailIntent,
            context.getString(R.string.settings_email_sender_title),
        ),
    )
}

internal fun hasStoragePermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else {
        context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

internal fun saveBackupFile(context: Context, jsonDatabase: String): File {
    val file =
        File(
            Environment.getExternalStorageDirectory()?.path + context.getString(R.string.backup_file_path),
            context.getString(R.string.backup_file_name),
        )
    file.parentFile?.mkdirs()
    file.writeText(jsonDatabase)
    return file
}

internal fun shareFile(context: Context, file: File) {
    val uri =
        FileProvider.getUriForFile(
            context,
            context.getString(R.string.file_provider_path),
            file,
        )

    val intent =
        Intent(Intent.ACTION_SEND).apply {
            type = context.getString(R.string.backup_file_type)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.backup_sharing_file_title))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.backup_sharing_file_description))
        }

    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.backup_sharing_file_choose)),
    )
}

internal fun readTextFromUri(context: Context, uri: Uri): String {
    return runCatching {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
    }.getOrDefault("")
}

internal fun openAppSettings(context: Context) {
    val intent =
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        )
    context.startActivity(intent)
}
