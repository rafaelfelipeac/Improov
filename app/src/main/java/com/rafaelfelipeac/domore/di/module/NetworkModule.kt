package com.rafaelfelipeac.domore.di.module

import com.rafaelfelipeac.domore.network.RMApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideHuntzApi(retrofit: Retrofit): RMApi {
        return retrofit.create(RMApi::class.java)
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
