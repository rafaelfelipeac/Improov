package com.rafaelfelipeac.mountains.app

import android.app.Application
import androidx.room.Room
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.persistence.database.RoomDatabase
import com.rafaelfelipeac.mountains.core.di.AppComponent
import com.rafaelfelipeac.mountains.core.di.DaggerAppComponent

val prefs: Prefs by lazy { App.prefs!! }

class App : Application() {
    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    companion object {
        var prefs: Prefs? = null
        var database: RoomDatabase? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)

        database = Room
            .databaseBuilder(this, RoomDatabase::class.java, getString(R.string.database_name))
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        super.onCreate()
    }
}
