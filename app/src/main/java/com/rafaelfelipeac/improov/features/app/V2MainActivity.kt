package com.rafaelfelipeac.improov.features.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.LocaleHelper

class V2MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        enableEdgeToEdge()

        setContent {
            ImproovAppRoot()
        }
    }
}
