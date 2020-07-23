package com.rafaelfelipeac.improov.core.di

import com.rafaelfelipeac.improov.app.App
import com.rafaelfelipeac.improov.core.di.modules.ContextModule
import com.rafaelfelipeac.improov.core.di.modules.PreferencesModule
import com.rafaelfelipeac.improov.core.di.modules.ViewModelModule
import com.rafaelfelipeac.improov.core.di.modules.GoalModule
import com.rafaelfelipeac.improov.core.di.modules.ProfileModule
import com.rafaelfelipeac.improov.core.di.modules.WelcomeModule
import com.rafaelfelipeac.improov.core.di.modules.PersistenceModule
import com.rafaelfelipeac.improov.core.di.modules.SettingsModule
import com.rafaelfelipeac.improov.core.di.modules.SplashModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    PreferencesModule::class,
    ViewModelModule::class,
    GoalModule::class,
    ProfileModule::class,
    WelcomeModule::class,
    PersistenceModule::class,
    SettingsModule::class,
    SplashModule::class
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
