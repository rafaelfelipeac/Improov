package com.rafaelfelipeac.improov.features.goal.presentation.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.commons.Goal
import com.rafaelfelipeac.improov.features.commons.GoalRepository
import com.rafaelfelipeac.improov.features.goal.*
import javax.inject.Inject

class GoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val itemRepository: ItemRepository,
    private val historicRepository: HistoricRepository
) : BaseViewModel() {

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null
    private var items: LiveData<List<Item>>? = null
    private var history: LiveData<List<Historic>>? = null

    init {
        goals = goalRepository.getGoals()
        items = itemRepository.getItems()
        history = historicRepository.getHistory()
    }

    fun init(goalId: Long) {
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
    fun getHistory(): LiveData<List<Historic>>? {
        return history
    }

    fun saveHistoric(historic: Historic) {
        historicRepository.save(historic)

        history = historicRepository.getHistory()
    }

    // Item
    fun getItems(): LiveData<List<Item>>? {
        return items
    }

    fun saveItem(item: Item) {
        itemRepository.save(item)

        items = itemRepository.getItems()
    }

    fun deleteItem(item: Item) {
        itemRepository.delete(item)

        items = itemRepository.getItems()
    }
}
