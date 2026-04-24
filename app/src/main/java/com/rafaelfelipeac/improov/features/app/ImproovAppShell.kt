package com.rafaelfelipeac.improov.features.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rafaelfelipeac.improov.core.ui.theme.ImproovTheme

@Composable
fun ImproovAppShell(content: @Composable () -> Unit) {
    ImproovTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )
    }
}
