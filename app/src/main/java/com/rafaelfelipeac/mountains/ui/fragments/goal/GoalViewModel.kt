package com.rafaelfelipeac.mountains.ui.fragments.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalViewModel: BaseViewModel() {

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