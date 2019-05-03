package com.rafaelfelipeac.mountains.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.database.historic.HistoricRepository
import com.rafaelfelipeac.mountains.database.item.ItemRepository
import com.rafaelfelipeac.mountains.di.component.DaggerViewModelInjector
import com.rafaelfelipeac.mountains.di.component.ViewModelInjector
import com.rafaelfelipeac.mountains.di.module.NetworkModule
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.models.Item

abstract class BaseViewModel : ViewModel() {

    val goalRepository: GoalRepository = GoalRepository(App.database?.goalDAO()!!)
    private val itemRepository: ItemRepository = ItemRepository(App.database?.itemDAO()!!)
    private val historicRepository: HistoricRepository = HistoricRepository(App.database?.historicDAO()!!)

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

    // Historic
    fun insertHistoric(historic: Historic) {
        historicRepository.insert(historic)
    }

    fun getHistorical(): List<Historic> {
        return  historicRepository.getHistorical()
    }

    fun updateHistoric(historic: Historic) {
        historicRepository.update(historic)
    }

    fun deleteHistoric(historic: Historic) {
        historicRepository.delete(historic)
    }

    // Item
    fun insertItem(item: Item) {
        itemRepository.insert(item)
    }

    fun updateItem(item: Item) {
        itemRepository.update(item)
    }

    fun getItems(): List<Item> {
        return itemRepository.getItems()
    }

    fun deleteItem(item: Item) {
        return itemRepository.delete(item)
    }

    // Goal
//    fun insertGoal(goal: Goal) {
//        goalRepository.insert(goal)
//    }
//
//    fun updateGoal(goal: Goal) {
//        goalRepository.update(goal)
//    }
//
//    fun getGoals(): List<Goal> {
//        return goalRepository.getGoals()
//    }
//
//    fun getGoals2(): LiveData<List<Goal>> {
//        return goalRepository.getGoals2()
//    }
//
//    fun deleteGoal(goal: Goal) {
//        goalRepository.delete(goal)
//    }
}