package com.rafaelfelipeac.improov.features.backup.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoalDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoricDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createItemDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createJson
import com.rafaelfelipeac.improov.base.DataProviderTest.getDate
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDAO
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDAO
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DatabaseRepositoryImplTest {

    @Mock
    internal lateinit var goalsDao: GoalDAO

    @Mock
    internal lateinit var historicDao: HistoricDAO

    @Mock
    internal lateinit var itemsDao: ItemDAO

    @Mock
    internal lateinit var preferences: Preferences

    private lateinit var databaseRepositoryImp: DatabaseRepositoryImpl

    @Before
    fun setup() {
        databaseRepositoryImp = DatabaseRepositoryImpl(goalsDao, historicDao, itemsDao, preferences)
    }

    @Test
    fun `GIVEN information in DAO and Preferences WHEN export is called THEN return a json for that information`() {
        runBlocking {
            // given
            val goals = listOf(createGoalDataModel())
            val historics = listOf(createHistoricDataModel())
            val items = listOf(createItemDataModel())
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

            given(goalsDao.getAll())
                .willReturn(goals)
            given(historicDao.getAll())
                .willReturn(historics)
            given(itemsDao.getAll())
                .willReturn(items)
            given(preferences.firstTimeAdd)
                .willReturn(firstTimeAdd)
            given(preferences.firstTimeList)
                .willReturn(firstTimeList)
            given(preferences.name)
                .willReturn(name)
            given(preferences.language)
                .willReturn(language)
            given(preferences.welcome)
                .willReturn(welcome)

            // when
            val result = databaseRepositoryImp.export()

            // then
            result equalTo json
        }
    }

    @Test
    fun `GIVEN a json with information for DAO and Preferences WHEN import is called THEN return true`() {
        runBlocking {
            // given
            val goals = listOf(createGoalDataModel())
            val historics = listOf(createHistoricDataModel())
            val items = listOf(createItemDataModel())
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

            // when
            val result = databaseRepositoryImp.import(json)

            // then
            result equalTo true
        }
    }

    @Test
    fun `GIVEN a json with invalid information WHEN import is called THEN return a false Boolean value`() {
        runBlocking {
            // given
            val json = "invalid json"

            // when
            val result = databaseRepositoryImp.import(json)

            // then
            result equalTo false
        }
    }

    @Test
    fun `GIVEN a Long value WHEN getExportDate is called THEN return the same Long value`() {
        runBlocking {
            // given
            val date = getDate()

            given(preferences.exportDate)
                .willReturn(date)

            // when
            val result = databaseRepositoryImp.getExportDate()

            // then
            result equalTo date
        }
    }

    @Test
    fun `GIVEN a Long value WHEN getImportDate is called THEN return the same Long value`() {
        runBlocking {
            // given
            val date = getDate()

            given(preferences.importDate)
                .willReturn(date)

            // when
            val result = databaseRepositoryImp.getImportDate()

            // then
            result equalTo date
        }
    }
}
