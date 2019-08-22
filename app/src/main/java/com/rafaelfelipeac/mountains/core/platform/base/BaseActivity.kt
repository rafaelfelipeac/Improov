package com.rafaelfelipeac.mountains.core.platform.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.features.settings.LocaleHelper

abstract class BaseActivity : AppCompatActivity() {

    val injector get() = (application as App).appComponent

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState, persistentState)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(context))
    }
}
