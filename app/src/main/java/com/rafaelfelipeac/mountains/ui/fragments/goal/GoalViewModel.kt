package com.rafaelfelipeac.mountains.ui.fragments.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.database.historic.HistoricRepository
import com.rafaelfelipeac.mountains.database.item.ItemRepository
import com.rafaelfelipeac.mountains.database.user.UserRepository
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class GoalViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val itemRepository: ItemRepository,
    private val historicRepository: HistoricRepository) : BaseViewModel() {

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null
    private var items: LiveData<List<Item>>? = null
    private var history: LiveData<List<Historic>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        getUser()

        goals = goalRepository.getGoals()
        items = itemRepository.getItems()
        history = historicRepository.getHistory()
    }

    fun init(goalId: Long) {
        goal = goalRepository.getGoal(goalId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
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
