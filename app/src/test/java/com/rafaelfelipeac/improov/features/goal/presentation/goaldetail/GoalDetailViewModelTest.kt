package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProvider.createGoal
import com.rafaelfelipeac.improov.base.DataProvider.createHistoric
import com.rafaelfelipeac.improov.base.DataProvider.createItem
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.SaveHistoricUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.SaveItemUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoalDetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveGoalUseCase = mock(SaveGoalUseCase::class.java)
    private var mockGetGoalUseCase = mock(GetGoalUseCase::class.java)
    private var mockSaveItemUseCase = mock(SaveItemUseCase::class.java)
    private var mockGetItemListUseCase = mock(GetItemListUseCase::class.java)
    private var mockSaveHistoricUseCase = mock(SaveHistoricUseCase::class.java)
    private var mockGetHistoricListUseCase = mock(GetHistoricListUseCase::class.java)

    private lateinit var goalDetailViewModel: GoalDetailViewModel

    @Before
    fun setup() {
        goalDetailViewModel = GoalDetailViewModel(
            mockSaveGoalUseCase,
            mockGetGoalUseCase,
            mockSaveItemUseCase,
            mockGetItemListUseCase,
            mockSaveHistoricUseCase,
            mockGetHistoricListUseCase
        )
    }

    @Test
    fun `GIVEN saveGoal is successful WHEN onSaveGoal is called THEN true is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase.execute(goal) })
            .willReturn(goalId)

        // when
        goalDetailViewModel.onSaveGoal(goal)

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = true,
            goal = Goal(),
            itemSaved = false,
            items = listOf(),
            historicSaved = false,
            historics = listOf()
        )
    }

    @Test
    fun `GIVEN getGoal is successful WHEN getGoal is called THEN return the specific goal`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockGetGoalUseCase.execute(goalId) })
            .willReturn(goal)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = false,
            goal = goal,
            itemSaved = false,
            items = listOf(),
            historicSaved = false,
            historics = listOf()
        )
    }

    @Test
    fun `GIVEN saveItem is successful WHEN onSaveItem is called THEN true is returned`() {
        // given
        val itemId = 1L
        val item = createItem(itemId)

        given(runBlocking { mockSaveItemUseCase.execute(item) })
            .willReturn(itemId)

        // when
        goalDetailViewModel.onSaveItem(item)

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            itemSaved = true,
            items = listOf(),
            historicSaved = false,
            historics = listOf()
        )
    }

    @Test
    fun `GIVEN getItemList is successful WHEN getItems is called THEN return a list of items`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)
        val items = listOf(
            createItem(itemId = 1, goalId = goalId),
            createItem(itemId = 2, goalId = goalId),
            createItem(itemId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetGoalUseCase.execute(goalId) })
            .willReturn(goal)
        given(runBlocking { mockGetItemListUseCase.execute(goalId) })
            .willReturn(items)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = false,
            goal = goal,
            itemSaved = false,
            items = items,
            historicSaved = false,
            historics = listOf()
        )
    }

    @Test
    fun `GIVEN saveHistoric is successful WHEN onSaveHistoric is called THEN true is returned`() {
        // given
        val historicId = 1L
        val historic = createHistoric(historicId)

        given(runBlocking { mockSaveHistoricUseCase.execute(historic) })
            .willReturn(historicId)

        // when
        goalDetailViewModel.onSaveHistoric(historic)

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            itemSaved = false,
            items = listOf(),
            historicSaved = true,
            historics = listOf()
        )
    }

    @Test
    fun `GIVEN getHistoricList is successful WHEN getHistoric is called THEN return a list of historics`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)
        val historics = listOf(
            createHistoric(historicId = 1, goalId = goalId),
            createHistoric(historicId = 2, goalId = goalId),
            createHistoric(historicId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetGoalUseCase.execute(goalId) })
            .willReturn(goal)
        given(runBlocking { mockGetHistoricListUseCase.execute(goalId) })
            .willReturn(historics)

        // when
        goalDetailViewModel.setGoalId(goalId)
        goalDetailViewModel.loadData()

        // then
        goalDetailViewModel.stateLiveData.value shouldBeEqualTo GoalDetailViewModel.ViewState(
            goalSaved = false,
            goal = goal,
            itemSaved = false,
            items = listOf(),
            historicSaved = false,
            historics = historics
        )
    }
}
