package com.rafaelfelipeac.mountains.ui.fragments.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalViewModel: BaseViewModel() {
    private var goalId: Long? = null

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null

    private var items: LiveData<List<Item>>? = null

    private var historical: LiveData<List<Historic>>? = null

    init {
        goals = goalRepository.getGoals()
        items = itemRepository.getItems()
        historical = historicRepository.getHistorical()
    }

    fun init(goalId: Long) {
        this.goalId = goalId

        goal = goalRepository.getGoal(goalId)
    }

    // Goal
    fun getGoal(): LiveData<Goal>? {
        return goal
    }

    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun saveGoal(goal: Goal) {
        goalRepository.save(goal)
    }

    // Historic
    fun saveHistoric(historic: Historic) {
        historicRepository.save(historic)
    }

    fun getHistorical(): LiveData<List<Historic>>? {
        return historical
    }
//
//    // Item
//    fun insertItem(item: Item) {
//        itemRepository.save(item)
//    }
//
//    fun updateItem(item: Item) {
//        itemRepository.update(item)
//    }
//
    // Item
    fun getItems(): LiveData<List<Item>>? {
        return items
    }

    fun saveItem(item: Item) {
        itemRepository.save(item)
    }

    fun deleteItem(item: Item) {
        itemRepository.delete(item)
    }
}