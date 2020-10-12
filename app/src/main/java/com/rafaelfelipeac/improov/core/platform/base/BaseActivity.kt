package com.rafaelfelipeac.improov.core.platform.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.improov.core.LocaleHelper
import com.rafaelfelipeac.improov.core.di.AppComponent
import com.rafaelfelipeac.improov.core.di.AppComponentProvider
import com.rafaelfelipeac.improov.core.di.provider.ViewModelProvider

abstract class BaseActivity : AppCompatActivity() {

    private val appComponent: AppComponent
        get() = (application as AppComponentProvider).appComponent
    val viewModelProvider: ViewModelProvider
        get() = appComponent

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(context))
    }
}
