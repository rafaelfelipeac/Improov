package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModelMapper
import com.rafaelfelipeac.improov.features.goal.data.GoalDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.doNothing
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoalDataSourceTest {

    @Mock
    internal lateinit var goalDao: GoalDao

    @Mock
    internal lateinit var goalDataModelMapper: GoalDataModelMapper

    private lateinit var goalRepositoryImp: GoalDataSource

    private val goalId = 1L

    @Before
    fun setup() {
        goalRepositoryImp = GoalDataSource(goalDao, goalDataModelMapper)
    }

    @Test
    fun `GIVEN a goalId WHEN getGoal is called THEN return a goal with the specific goalId`() {
        runBlocking {
            // given
            val goal = createGoal(goalId).let { goalDataModelMapper.mapReverse(it) }
            given(goalDao.get(goalId))
                .willReturn(goal)

            // when
            val result = goalRepositoryImp.getGoal(goalId)

            // then
            result equalTo goal
        }
    }

    @Test
    fun `GIVEN a list of goals WHEN getGoals is called THEN return a list with the same goals`() {
        runBlocking {
            // given
            val goals = listOf(createGoal(), createGoal(), createGoal()).let { goalDataModelMapper.mapListReverse(it) }
            given(goalDao.getAll())
                .willReturn(goals)

            // when
            val result = goalRepositoryImp.getGoals()

            // then
            result equalTo goals
        }
    }

    @Test
    fun `GIVEN a goal WHEN save is called THEN return the goalId as a confirmation`() {
        runBlocking {
            // given
            val goal = createGoal(goalId)
            given(goalDao.save(goal.let { goalDataModelMapper.mapReverse(it) }))
                .willReturn(goalId)

            // when
            val result = goalRepositoryImp.save(goal)

            // then
            result equalTo goalId
        }
    }

    @Test
    fun `GIVEN a goal with a specific goalId WHEN delete is called THEN return just a Unit value`() {
        runBlocking {
            // given
            val goal = createGoal(goalId)
            val goalReverse = goal.let { goalDataModelMapper.mapReverse(it) }
            doNothing().`when`(goalDao).delete(goalReverse)

            // when
            val result = goalRepositoryImp.delete(goal)

            // then
            result equalTo Unit
        }
    }
}
