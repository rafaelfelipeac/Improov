package com.rafaelfelipeac.mountains.network

import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Habit
import io.reactivex.Observable
import retrofit2.http.GET

interface GoalHabitApi {

    @GET("/goals")
    fun getGoals(): Observable<List<Goal>>

    @GET("/habits")
    fun getHabits(): Observable<List<Habit>>
}