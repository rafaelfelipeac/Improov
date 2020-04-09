package com.rafaelfelipeac.improov.features.goal.domain.usecase.goal

import com.rafaelfelipeac.improov.base.DataProvider.createGoal
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteGoalUseCaseTest {

    @Mock
    internal lateinit var mockGoalRepository: GoalRepository

    private lateinit var deleteGoalUseCase: DeleteGoalUseCase

    private var goalId = 1L

    @Before
    fun setup() {
        deleteGoalUseCase = DeleteGoalUseCase(mockGoalRepository)
    }

    @Test
    fun `GIVEN a goalId WHEN use deleteGoalUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val goal = createGoal(goalId)
            given(mockGoalRepository.delete(goal))
                .willReturn(Unit)

            // when
            val result = deleteGoalUseCase.execute(goal)

            // then
            result shouldBeEqualTo Unit
        }
    }
}
