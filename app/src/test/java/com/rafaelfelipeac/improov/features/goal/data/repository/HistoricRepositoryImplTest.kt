package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModelMapper
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
    internal lateinit var historicDAO: HistoricDAO

    @Mock
    internal lateinit var historicDataModelMapper: HistoricDataModelMapper

    private lateinit var historicRepositoryImp: HistoricRepositoryImpl

    private val historicId = 1L

    @Before
    fun setup() {
        historicRepositoryImp = HistoricRepositoryImpl(historicDAO, historicDataModelMapper)
    }

    @Test
    fun `GIVEN a historicId WHEN getHistoric is called THEN historicRepositoryImpl return a historic`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId).let { historicDataModelMapper.mapReverse(it) }
            given(historicDAO.get(historicId))
                .willReturn(historic)

            // when
            val result = historicRepositoryImp.getHistoric(historicId)

            // then
            result shouldBeEqualTo historic
        }
    }

    @Test
    fun `GIVEN a list of historics WHEN getHistorics is called THEN historicRepositoryImpl return the list`() {
        runBlocking {
            // given
            val historics = listOf(createHistoric(), createHistoric(), createHistoric())
                .let { historicDataModelMapper.mapListReverse(it) }
            given(historicDAO.getAll())
                .willReturn(historics)

            // when
            val result = historicRepositoryImp.getHistorics()

            // then
            result shouldBeEqualTo historics
        }
    }

    @Test
    fun `GIVEN a historic WHEN save is called THEN historicRepositoryImp return the historicId as a confirmation`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(historicDAO.save(historic.let { historicDataModelMapper.mapReverse(it) }))
                .willReturn(historicId)

            // when
            val result = historicRepositoryImp.save(historic)

            // then
            result shouldBeEqualTo historicId
        }
    }

    @Test
    fun `GIVEN a historic WHEN delete is called THEN historicRepositoryImp return just a Unit value`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            val historicReverse = historic.let { historicDataModelMapper.mapReverse(it) }
            doNothing().`when`(historicDAO).delete(historicReverse)

            // when
            val result = historicRepositoryImp.delete(historic)

            // then
            result shouldBeEqualTo Unit
        }
    }
}
