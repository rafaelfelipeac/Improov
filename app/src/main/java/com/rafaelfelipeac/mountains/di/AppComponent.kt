package com.rafaelfelipeac.mountains.di

import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.di.modules.ListModule
import com.rafaelfelipeac.mountains.di.modules.PersistenceModule
import com.rafaelfelipeac.mountains.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ViewModelModule::class,
    ListModule::class,
    PersistenceModule::class
])
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
