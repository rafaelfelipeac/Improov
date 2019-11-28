package com.rafaelfelipeac.improov.core.di.modules

import android.content.Context
import com.rafaelfelipeac.improov.app.App
import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {

    @Binds
    abstract fun bindContext(app: App): Context
}
