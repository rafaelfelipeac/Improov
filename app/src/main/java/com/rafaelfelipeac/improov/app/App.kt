package com.rafaelfelipeac.improov.app

import android.app.Application
import com.rafaelfelipeac.improov.core.di.AppComponent
import com.rafaelfelipeac.improov.core.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}
