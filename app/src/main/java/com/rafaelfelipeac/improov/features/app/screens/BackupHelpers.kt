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
import com.rafaelfelipeac.improov.R

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

internal fun readTextFromUri(context: Context, uri: Uri): String {
    return runCatching {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
    }.getOrDefault("")
}

internal fun writeTextToUri(context: Context, uri: Uri, text: String): Boolean {
    return runCatching {
        context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(text) }
        true
    }.getOrDefault(false)
}
