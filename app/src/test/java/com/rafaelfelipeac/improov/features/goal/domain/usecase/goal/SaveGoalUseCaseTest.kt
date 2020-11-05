package com.rafaelfelipeac.improov.features.goal.domain.usecase.goal

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveGoalUseCaseTest {

    @Mock
    internal lateinit var mockGoalRepository: GoalRepository

    private lateinit var saveGoalUseCase: SaveGoalUseCase

    private var goalId = 1L

    @Before
    fun setup() {
        saveGoalUseCase = SaveGoalUseCase(mockGoalRepository)
    }

    @Test
    fun `GIVEN a goalId WHEN saveGoalUseCase is called THEN return the same goalId as a confirmation`() {
        runBlocking {
            // given
            val goal = createGoal(goalId)
            given(mockGoalRepository.save(goal))
                .willReturn(goalId)

            // when
            val result = saveGoalUseCase(goal)

            // then
            result equalTo goalId
        }
    }
}
