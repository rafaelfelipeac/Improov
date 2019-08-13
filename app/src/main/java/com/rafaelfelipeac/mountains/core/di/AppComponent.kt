package com.rafaelfelipeac.mountains.core.di

import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.core.di.modules.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    PreferencesModule::class,
    ViewModelModule::class,
    CreateUserModule::class,
    EditProfileModule::class,
    ForgotPasswordModule::class,
    GoalFormModule::class,
    GoalModule::class,
    HabitFormModule::class,
    HabitModule::class,
    ListModule::class,
    LoginModule::class,
    ProfileModule::class,
    StatsModule::class,
    TodayModule::class,
    WelcomeModule::class,
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
