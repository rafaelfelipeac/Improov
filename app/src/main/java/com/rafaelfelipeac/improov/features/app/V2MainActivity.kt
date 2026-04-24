package com.rafaelfelipeac.improov.features.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafaelfelipeac.improov.core.platform.base.BaseActivity

class V2MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ImproovAppRoot()
        }
    }
}
