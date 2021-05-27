package com.rafaelfelipeac.improov.core.di

import android.app.Application
import com.rafaelfelipeac.improov.core.di.modules.CoreModule
import com.rafaelfelipeac.improov.core.di.modules.PreferencesModule
import com.rafaelfelipeac.improov.core.di.modules.GoalModule
import com.rafaelfelipeac.improov.core.di.modules.ProfileModule
import com.rafaelfelipeac.improov.core.di.modules.WelcomeModule
import com.rafaelfelipeac.improov.core.di.modules.PersistenceModule
import com.rafaelfelipeac.improov.core.di.modules.SettingsModule
import com.rafaelfelipeac.improov.core.di.modules.SplashModule
import com.rafaelfelipeac.improov.core.di.modules.BackupModule
import com.rafaelfelipeac.improov.core.di.modules.HabitModule
import com.rafaelfelipeac.improov.core.di.modules.StatsModule
import com.rafaelfelipeac.improov.core.di.modules.TodayModule
import com.rafaelfelipeac.improov.core.di.provider.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    CoreModule::class,
    PreferencesModule::class,
    GoalModule::class,
    ProfileModule::class,
    WelcomeModule::class,
    PersistenceModule::class,
    SettingsModule::class,
    SplashModule::class,
    BackupModule::class,
    HabitModule::class,
    StatsModule::class,
    TodayModule::class
])
@Singleton
interface AppComponent : ViewModelProvider {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}

interface AppComponentProvider {
    val appComponent: AppComponent
}
