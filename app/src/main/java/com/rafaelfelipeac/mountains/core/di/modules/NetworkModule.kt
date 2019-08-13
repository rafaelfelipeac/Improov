package com.rafaelfelipeac.mountains.core.di.modules

import com.rafaelfelipeac.mountains.network.retrofit.GoalHabitApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideGoalHabitApi(retrofit: Retrofit): GoalHabitApi {
        return retrofit.create(GoalHabitApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}
