package com.rafaelfelipeac.improov

import android.app.Application
import com.rafaelfelipeac.improov.core.di.AppComponent
import com.rafaelfelipeac.improov.core.di.AppComponentProvider
import com.rafaelfelipeac.improov.core.di.DaggerAppComponent

class App : Application(), AppComponentProvider {

    override val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }
}
