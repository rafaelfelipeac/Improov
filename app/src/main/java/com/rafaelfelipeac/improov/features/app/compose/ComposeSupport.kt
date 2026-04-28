package com.rafaelfelipeac.improov.features.app.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rafaelfelipeac.improov.core.di.AppComponentProvider
import com.rafaelfelipeac.improov.core.di.provider.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

internal tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Composable
internal fun rememberAppViewModelProvider(): ViewModelProvider? {
    val context = LocalContext.current
    val application = context.applicationContext as? AppComponentProvider ?: return null

    return remember(application) { application.appComponent }
}

@Composable
internal fun <T> rememberAppViewModel(
    factory: ViewModelProvider.() -> T
): T? {
    val provider = rememberAppViewModelProvider() ?: return null

    return remember(provider) { provider.factory() }
}

@Composable
internal fun <T> Flow<T>.collectAsStateCompat(initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }

    LaunchedEffect(this) {
        collect { value ->
            state.value = value
        }
    }

    return state
}
