package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModelMapper
import com.rafaelfelipeac.improov.features.goal.data.HistoricDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HistoricDataSourceTest {

    @Mock
    internal lateinit var historicDao: HistoricDao

    @Mock
    internal lateinit var historicDataModelMapper: HistoricDataModelMapper

    private lateinit var historicDataSource: HistoricDataSource

    private val historicId = 1L

    @Before
    fun setup() {
        historicDataSource = HistoricDataSource(historicDao, historicDataModelMapper)
    }

    @Test
    fun `GIVEN a historicId WHEN getHistoric is called THEN return a historic with the specific historicId`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId).let { historicDataModelMapper.mapReverse(it) }
            given(historicDao.get(historicId))
                .willReturn(historic)

            // when
            val result = historicDataSource.getHistoric(historicId)

            // then
            result equalTo historic
        }
    }

    @Test
    fun `GIVEN a list of historics WHEN getHistorics is called THEN return the same historics`() {
        runBlocking {
            // given
            val historics = listOf(createHistoric(), createHistoric(), createHistoric())
                .let { historicDataModelMapper.mapListReverse(it) }
            given(historicDao.getAll())
                .willReturn(historics)

            // when
            val result = historicDataSource.getHistorics()

            // then
            result equalTo historics
        }
    }

    @Test
    fun `GIVEN a historic WHEN save is called THEN return the historicId as a confirmation`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(historicDao.save(historic.let { historicDataModelMapper.mapReverse(it) }))
                .willReturn(historicId)

            // when
            val result = historicDataSource.save(historic)

            // then
            result equalTo historicId
        }
    }

    @Test
    fun `GIVEN a historic WHEN delete is called THEN return just a Unit value`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            val historicReverse = historic.let { historicDataModelMapper.mapReverse(it) }
            doNothing().`when`(historicDao).delete(historicReverse)

            // when
            val result = historicDataSource.delete(historic)

            // then
            result equalTo Unit
        }
    }
}
