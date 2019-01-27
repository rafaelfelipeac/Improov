package com.rafaelfelipeac.domore.app

import android.app.Application
import androidx.room.Room
import com.rafaelfelipeac.domore.database.AppDatabase
import com.rafaelfelipeac.domore.di.AppComponent
import com.rafaelfelipeac.domore.di.DaggerAppComponent

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

        database = Room
            .databaseBuilder(this, AppDatabase::class.java, "rm-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        super.onCreate()
    }
}
