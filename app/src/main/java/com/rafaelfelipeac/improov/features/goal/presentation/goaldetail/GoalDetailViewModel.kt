package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import javax.inject.Inject

class GoalDetailViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val itemRepository: ItemRepository,
    private val historicRepository: HistoricRepository
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    GoalListViewModel.ViewState()
) {
    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null
    private var items: LiveData<List<Item>>? = null
    private var history: LiveData<List<Historic>>? = null

    init {
        //goals = goalRepository.getGoals()
//        items = itemRepository.getItems()
//        history = historicRepository.getHistory()
    }

    fun init(goalId: Long) {
        //goal = goalRepository.getGoal(goalId)
    }

    // Goal
    fun getGoal(): LiveData<Goal>? {
        return goal
    }

    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun saveGoal(goal: Goal) {
        //goalRepository.save(goal)
    }

    // Historic
    fun getHistory(): LiveData<List<Historic>>? {
        return history
    }

    fun saveHistoric(historic: Historic) {
//        historicRepository.save(historic)
//
//        history = historicRepository.getHistory()
    }

    // Item
    fun getItems(): LiveData<List<Item>>? {
        return items
    }

    fun saveItem(item: Item) {
//        itemRepository.save(item)
//
//        items = itemRepository.getItems()
    }

    fun deleteItem(item: Item) {
//        itemRepository.delete(item)
//
//        items = itemRepository.getItems()
    }

    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
