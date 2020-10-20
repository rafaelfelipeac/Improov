package com.rafaelfelipeac.improov.features.backup.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest
import com.rafaelfelipeac.improov.base.DataProviderTest.getDate
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ExportDatabaseUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetExportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.GetImportDateUseCase
import com.rafaelfelipeac.improov.features.backup.domain.usecase.ImportDatabaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BackupViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockExportDatabaseUseCase = mock(ExportDatabaseUseCase::class.java)
    private var mockImportDatabaseUseCase = mock(ImportDatabaseUseCase::class.java)
    private var mockGetExportDateUseCase = mock(GetExportDateUseCase::class.java)
    private var mockGetImportDateUseCase = mock(GetImportDateUseCase::class.java)

    private lateinit var backupViewModel: BackupViewModel

    @Before
    fun setup() {
        backupViewModel = BackupViewModel(
            mockExportDatabaseUseCase,
            mockImportDatabaseUseCase,
            mockGetExportDateUseCase,
            mockGetImportDateUseCase
        )
    }

    @Test
    fun `GIVEN export is successful WHEN exportDatabase is called THEN the json for the database is returned`() {
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

        given(runBlocking { mockExportDatabaseUseCase() })
            .willReturn(json)

        // when
        backupViewModel.exportDatabase()

        // then
        backupViewModel.export.value equalTo json
    }

    @Test
    fun `GIVEN a valid json WHEN importDatabase is called THEN true is returned`() {
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

        given(runBlocking { mockImportDatabaseUseCase(json) })
            .willReturn(true)

        // when
        backupViewModel.importDatabase(json)

        // then
        backupViewModel.import.value equalTo true
    }

    @Test
    fun `GIVEN a invalid json WHEN importDatabase is called THEN true is returned`() {
        // given
        val json = "invalid json"

        given(runBlocking { mockImportDatabaseUseCase(json) })
            .willReturn(false)

        // when
        backupViewModel.importDatabase(json)

        // then
        backupViewModel.import.value equalTo false
    }

    @Test
    fun `GIVEN exportDate is successful WHEN getExportDate is called THEN the exported date is returned`() {
        // given
        val exportDate = getDate()

        given(runBlocking { mockGetExportDateUseCase() })
            .willReturn(exportDate)

        // when
        backupViewModel.getExportDate()

        // then
        backupViewModel.exportDate.value equalTo exportDate
    }

    @Test
    fun `GIVEN importDate is successful WHEN getImportDate is called THEN the imported date is returned`() {
        // given
        val importDate = getDate()

        given(runBlocking { mockGetImportDateUseCase() })
            .willReturn(importDate)

        // when
        backupViewModel.getImportDate()

        // then
        backupViewModel.importDate.value equalTo importDate
    }
}
