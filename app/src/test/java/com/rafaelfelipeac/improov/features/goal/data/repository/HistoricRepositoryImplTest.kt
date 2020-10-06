package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModelMapper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HistoricRepositoryImplTest {

    @Mock
    internal lateinit var historicDao: HistoricDao

    @Mock
    internal lateinit var historicDataModelMapper: HistoricDataModelMapper

    private lateinit var historicRepositoryImp: HistoricRepositoryImpl

    private val historicId = 1L

    @Before
    fun setup() {
        historicRepositoryImp = HistoricRepositoryImpl(historicDao, historicDataModelMapper)
    }

    @Test
    fun `GIVEN a historicId WHEN getHistoric is called THEN historicRepositoryImpl return a historic`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId).let { historicDataModelMapper.mapReverse(it) }
            given(historicDao.get(historicId))
                .willReturn(historic)

            // when
            val result = historicRepositoryImp.getHistoric(historicId)

            // then
            result equalTo historic
        }
    }

    @Test
    fun `GIVEN a list of historics WHEN getHistorics is called THEN historicRepositoryImpl return the list`() {
        runBlocking {
            // given
            val historics = listOf(createHistoric(), createHistoric(), createHistoric())
                .let { historicDataModelMapper.mapListReverse(it) }
            given(historicDao.getAll())
                .willReturn(historics)

            // when
            val result = historicRepositoryImp.getHistorics()

            // then
            result equalTo historics
        }
    }

    @Test
    fun `GIVEN a historic WHEN save is called THEN historicRepositoryImp return the historicId as a confirmation`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(historicDao.save(historic.let { historicDataModelMapper.mapReverse(it) }))
                .willReturn(historicId)

            // when
            val result = historicRepositoryImp.save(historic)

            // then
            result equalTo historicId
        }
    }

    @Test
    fun `GIVEN a historic WHEN delete is called THEN historicRepositoryImp return just a Unit value`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            val historicReverse = historic.let { historicDataModelMapper.mapReverse(it) }
            doNothing().`when`(historicDao).delete(historicReverse)

            // when
            val result = historicRepositoryImp.delete(historic)

            // then
            result equalTo Unit
        }
    }
}
