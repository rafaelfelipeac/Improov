package com.rafaelfelipeac.improov.features.goal.domain.usecase.goal

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetGoalUseCaseTest {

    @Mock
    internal lateinit var mockGoalRepository: GoalRepository

    private lateinit var getGoalUseCase: GetGoalUseCase

    private val goalId = 1L

    @Before
    fun setup() {
        getGoalUseCase = GetGoalUseCase(mockGoalRepository)
    }

    @Test
    fun `GIVEN a goalId WHEN use getGoalUseCase THEN return a goal with the specific goalId`() {
        runBlocking {
            // given
            val goal = createGoal(goalId)
            given(mockGoalRepository.getGoal(goalId))
                .willReturn(goal)

            // when
            val result = getGoalUseCase(goalId)

            // then
            result shouldBeEqualTo goal
        }
    }
}
