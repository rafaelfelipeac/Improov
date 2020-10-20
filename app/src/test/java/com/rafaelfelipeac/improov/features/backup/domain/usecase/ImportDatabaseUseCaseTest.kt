package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImportDatabaseUseCaseTest {

    @Mock
    internal lateinit var mockDatabaseRepository: DatabaseRepository

    private lateinit var importDatabaseUseCase: ImportDatabaseUseCase

    @Before
    fun setup() {
        importDatabaseUseCase = ImportDatabaseUseCase(mockDatabaseRepository)
    }

    @Test
    fun `GIVEN a json with information for Dao and Preferences WHEN import is called THEN return true`() {
        runBlocking {
            // given
            val goals = listOf(DataProviderTest.createGoalDataModel())
            val historics = listOf(DataProviderTest.createHistoricDataModel())
            val items = listOf(DataProviderTest.createItemDataModel())
            val language = ""
            val welcome = false
            val name = ""
            val firstTimeAdd = false
            val firstTimeList = false

            val json = DataProviderTest.createJson(
                goals,
                historics,
                items,
                language,
                welcome,
                name,
                firstTimeAdd,
                firstTimeList
            )

            given(mockDatabaseRepository.import(json))
                .willReturn(true)

            // when
            val result = importDatabaseUseCase(json)

            // then
            result equalTo true
        }
    }

    @Test
    fun `GIVEN a json with invalid information WHEN import is called THEN return a false Boolean value`() {
        runBlocking {
            // given
            val json = "invalid json"

            given(mockDatabaseRepository.import(json))
                .willReturn(false)

            // when
            val result = importDatabaseUseCase(json)

            // then
            result equalTo false
        }
    }
}
