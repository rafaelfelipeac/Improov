package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest
import com.rafaelfelipeac.improov.base.DataProviderTest.createJson
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExportDatabaseUseCaseTest {

    @Mock
    internal lateinit var mockDatabaseRepository: DatabaseRepository

    private lateinit var exportDatabaseUseCase: ExportDatabaseUseCase

    @Before
    fun setup() {
        exportDatabaseUseCase = ExportDatabaseUseCase(mockDatabaseRepository)
    }

    @Test
    fun `GIVEN information in Dao and Preferences WHEN use exportDatabaseUseCase THEN return a json`() {
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

            val json = createJson(
                goals,
                historics,
                items,
                language,
                welcome,
                name,
                firstTimeAdd,
                firstTimeList
            )

            given(mockDatabaseRepository.export())
                .willReturn(json)

            // when
            val result = exportDatabaseUseCase()

            // then
            result equalTo json
        }
    }
}
