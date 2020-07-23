package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createHistoricDataModel
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoricDAOTest : BaseInstTest() {

    private val historicDAO get() = roomDatabase.historicDAO()

    @Test
    fun givenANewHistoricWhenGetIsCalledThenTheSameHistoricIsReturned() {
        // given
        val historicId = 1L
        val historic = createHistoricDataModel(historicId)

        historicDAO.save(historic)

        // when
        val result = historicDAO.get(historicId)

        // then
        result shouldBeEqualTo historic
    }

    @Test
    fun givenTwoNewHistoricsWhenGetAllIsCalledThenAListWithTheTwoHistoricsAreReturned() {
        // given
        val historicA = createHistoricDataModel(historicId = 1)
        val historicB = createHistoricDataModel(historicId = 2)
        val list = listOf(historicA, historicB)

        historicDAO.save(historicA)
        historicDAO.save(historicB)

        // when
        val result = historicDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenANewHistoricWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val historic = createHistoricDataModel()

        historicDAO.save(historic)

        // when
        val result = historicDAO.delete(historic)

        // then
        result shouldBeEqualTo Unit
    }

    @Test
    fun givenTwoNewHistoricsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneHistoricIsReturned() {
        // given
        val historicA = createHistoricDataModel(historicId = 1)
        val historicB = createHistoricDataModel(historicId = 2)
        val list = mutableListOf(historicA, historicB)

        historicDAO.save(historicA)
        historicDAO.save(historicB)

        list.remove(historicA)
        historicDAO.delete(historicA)

        // when
        val result = historicDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAHistoricWhenGetIsCalledThenItMustReturnedTheHistoricUpdated() {
        // given
        val historicId = 10L
        val historic = createHistoricDataModel(historicId = historicId)
        historicDAO.save(historic)

        historic.value = 10f
        historicDAO.save(historic)

        // when
        val result = historicDAO.get(historicId)

        // then
        result shouldBeEqualTo historic
    }
}
