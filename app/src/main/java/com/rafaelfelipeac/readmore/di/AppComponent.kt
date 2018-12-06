package com.rafaelfelipeac.readmore.di

import com.rafaelfelipeac.readmore.app.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component()
@Singleton
interface AppComponent : Injector {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: App): Builder
    }

    fun inject(application: App)
}
