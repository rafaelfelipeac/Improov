package com.rafaelfelipeac.improov.features.commons.data.dao

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createGoalDataModel
import com.rafaelfelipeac.improov.base.equal
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class GoalDaoTest : BaseInstTest() {

    private val goalDao get() = roomDatabase.goalDao()

    @Test
    fun givenANewGoalWhenGetIsCalledThenTheSameGoalIsReturned() {
        // given
        val goalId = 1L
        val goal = createGoalDataModel(goalId)

        goalDao.save(goal)

        // when
        val result = goalDao.get(goalId)

        // then
        result equal goal
    }

    @Test
    fun givenTwoNewGoalsWhenGetAllIsCalledThenAListWithTheTwoGoalsAreReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = listOf(goalA, goalB)

        goalDao.save(goalA)
        goalDao.save(goalB)

        // when
        val result = goalDao.getAll()

        // then
        result equal list
    }

    @Test
    fun givenANewGoalWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val goal = createGoalDataModel()

        goalDao.save(goal)

        // when
        val result = goalDao.delete(goal)

        // then
        result equal Unit
    }

    @Test
    fun givenTwoNewGoalsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneGoalIsReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = mutableListOf(goalA, goalB)

        goalDao.save(goalA)
        goalDao.save(goalB)

        list.remove(goalA)
        goalDao.delete(goalA)

        // when
        val result = goalDao.getAll()

        // then
        result equal list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAGoalWhenGetIsCalledThenItMustReturnedTheGoalUpdated() {
        // given
        val goalId = 10L
        val goalA = createGoalDataModel(goalId = goalId, name = "goalA")
        goalDao.save(goalA)

        goalA.name = "goalAUpdated"
        goalDao.save(goalA)

        // when
        val result = goalDao.get(goalId)

        // then
        result equal goalA
    }
}
