package com.rafaelfelipeac.mountains.ui.base

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.database.historic.HistoricRepository
import com.rafaelfelipeac.mountains.database.item.ItemRepository
import com.rafaelfelipeac.mountains.database.user.UserRepository
import com.rafaelfelipeac.mountains.di.component.DaggerViewModelInjector
import com.rafaelfelipeac.mountains.di.component.ViewModelInjector
import com.rafaelfelipeac.mountains.di.module.NetworkModule

abstract class BaseViewModel : ViewModel() {

    val goalRepository: GoalRepository = GoalRepository(App.database?.goalDAO()!!)
    val itemRepository: ItemRepository = ItemRepository(App.database?.itemDAO()!!)
    val historicRepository: HistoricRepository = HistoricRepository(App.database?.historicDAO()!!)
    val userRepository: UserRepository = UserRepository(App.database?.userDAO()!!)

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        injector.inject(this)
    }
}