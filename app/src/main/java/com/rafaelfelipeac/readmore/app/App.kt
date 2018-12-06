package com.rafaelfelipeac.readmore.app

import android.app.Application
import com.rafaelfelipeac.readmore.di.AppComponent
import com.rafaelfelipeac.readmore.di.DaggerAppComponent

val prefs: Prefs by lazy {
    App.prefs!!
}

class App : Application() {
    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}
