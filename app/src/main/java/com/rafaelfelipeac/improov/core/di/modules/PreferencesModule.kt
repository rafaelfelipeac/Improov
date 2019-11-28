package com.rafaelfelipeac.improov.core.di.modules

import android.content.Context
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import dagger.Module
import dagger.Provides

@Module
object PreferencesModule {

    @Provides
    @JvmStatic
    fun provideSharedPreferences(context: Context): Preferences =
        Preferences(context)
}
