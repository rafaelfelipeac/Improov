package com.rafaelfelipeac.mountains.app

import android.app.Application
import com.rafaelfelipeac.mountains.core.di.AppComponent
import com.rafaelfelipeac.mountains.core.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}
