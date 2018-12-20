package com.rafaelfelipeac.readmore.app

import android.app.Application
import android.arch.persistence.room.Room
import com.rafaelfelipeac.readmore.database.AppDatabase
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
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "rm-db")
            .allowMainThreadQueries()
            .build()

        super.onCreate()
    }
}
