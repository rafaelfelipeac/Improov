package com.rafaelfelipeac.improov.core.di.modules

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CoreModule {

    companion object {

        @Provides
        fun gson(): Gson = Gson()
    }

    @Binds
    abstract fun Application.context(): Context
}
