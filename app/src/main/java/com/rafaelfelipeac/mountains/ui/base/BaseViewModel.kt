package com.rafaelfelipeac.mountains.ui.base

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.database.habit.HabitRepository
import com.rafaelfelipeac.mountains.database.historic.HistoricRepository
import com.rafaelfelipeac.mountains.database.item.ItemRepository
import com.rafaelfelipeac.mountains.database.user.UserRepository

abstract class BaseViewModel : ViewModel() {

    val goalRepository = GoalRepository(App.database?.goalDAO()!!)
    val itemRepository = ItemRepository(App.database?.itemDAO()!!)
    val habitRepository = HabitRepository(App.database?.habitDAO()!!)
    val userRepository: UserRepository = UserRepository(App.database?.userDAO()!!)
    val historicRepository: HistoricRepository = HistoricRepository(App.database?.historicDAO()!!)

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

//    private val injector: ViewModelInjector = DaggerViewModelInjector
//        .builder()
//        .networkModule(NetworkModule)
//        .build()

    init {
        inject()
    }

    private fun inject() {
//        injector.inject(this)
    }
}