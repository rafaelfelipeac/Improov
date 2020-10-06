package com.rafaelfelipeac.improov.features.commons.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createHistoricDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoricDaoTest : BaseInstTest() {

    private val historicDao get() = roomDatabase.historicDao()

    @Test
    fun givenANewHistoricWhenGetIsCalledThenTheSameHistoricIsReturned() {
        // given
        val historicId = 1L
        val historic = createHistoricDataModel(historicId)

        historicDao.save(historic)

        // when
        val result = historicDao.get(historicId)

        // then
        result equalTo historic
    }

    @Test
    fun givenTwoNewHistoricsWhenGetAllIsCalledThenAListWithTheTwoHistoricsAreReturned() {
        // given
        val historicA = createHistoricDataModel(historicId = 1)
        val historicB = createHistoricDataModel(historicId = 2)
        val list = listOf(historicA, historicB)

        historicDao.save(historicA)
        historicDao.save(historicB)

        // when
        val result = historicDao.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenANewHistoricWhenDeleteIsCalledThenAUnitValueIsReturned() {
        // given
        val historic = createHistoricDataModel()

        historicDao.save(historic)

        // when
        val result = historicDao.delete(historic)

        // then
        result equalTo Unit
    }

    @Test
    fun givenTwoNewHistoricsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneHistoricIsReturned() {
        // given
        val historicA = createHistoricDataModel(historicId = 1)
        val historicB = createHistoricDataModel(historicId = 2)
        val list = mutableListOf(historicA, historicB)

        historicDao.save(historicA)
        historicDao.save(historicB)

        list.remove(historicA)
        historicDao.delete(historicA)

        // when
        val result = historicDao.getAll()

        // then
        result equalTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAHistoricWhenGetIsCalledThenItMustReturnedTheHistoricUpdated() {
        // given
        val historicId = 10L
        val historic = createHistoricDataModel(historicId = historicId)
        historicDao.save(historic)

        historic.value = 10f
        historicDao.save(historic)

        // when
        val result = historicDao.get(historicId)

        // then
        result equalTo historic
    }
}
