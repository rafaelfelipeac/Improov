package com.rafaelfelipeac.domore.network

import io.reactivex.Completable
import retrofit2.http.GET

interface RMApi {
    @GET("/verify")
    fun verify(): Completable
}