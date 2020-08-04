package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createGoalDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class GoalDAOTest : BaseInstTest() {

    private val goalDAO get() = roomDatabase.goalDAO()

    @Test
    fun givenANewGoalWhenGetIsCalledThenTheSameGoalIsReturned() {
        // given
        val goalId = 1L
        val goal = createGoalDataModel(goalId)

        goalDAO.save(goal)

        // when
        val result = goalDAO.get(goalId)

        // then
        result equalTo goal
    }

    @Test
    fun givenTwoNewGoalsWhenGetAllIsCalledThenAListWithTheTwoGoalsAreReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = listOf(goalA, goalB)

        goalDAO.save(goalA)
        goalDAO.save(goalB)

        // when
        val result = goalDAO.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenANewGoalWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val goal = createGoalDataModel()

        goalDAO.save(goal)

        // when
        val result = goalDAO.delete(goal)

        // then
        result equalTo Unit
    }

    @Test
    fun givenTwoNewGoalsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneGoalIsReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = mutableListOf(goalA, goalB)

        goalDAO.save(goalA)
        goalDAO.save(goalB)

        list.remove(goalA)
        goalDAO.delete(goalA)

        // when
        val result = goalDAO.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAGoalWhenGetIsCalledThenItMustReturnedTheGoalUpdated() {
        // given
        val goalId = 10L
        val goalA = createGoalDataModel(goalId = goalId, name = "goalA")
        goalDAO.save(goalA)

        goalA.name = "goalAUpdated"
        goalDAO.save(goalA)

        // when
        val result = goalDAO.get(goalId)

        // then
        result equalTo goalA
    }
}
