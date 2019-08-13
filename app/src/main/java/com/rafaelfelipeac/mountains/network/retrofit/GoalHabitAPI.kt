package com.rafaelfelipeac.mountains.network.retrofit

import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.Habit
import io.reactivex.Observable
import retrofit2.http.GET

interface GoalHabitApi {

    @GET("/goals")
    fun getGoals(): Observable<List<Goal>>

    @GET("/habits")
    fun getHabits(): Observable<List<Habit>>
}