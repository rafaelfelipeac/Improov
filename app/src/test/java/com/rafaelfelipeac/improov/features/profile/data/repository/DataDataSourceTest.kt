package com.rafaelfelipeac.improov.features.profile.data.repository

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoalDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoricDataModel
import com.rafaelfelipeac.improov.base.DataProviderTest.createItemDataModel
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModelMapper
import com.rafaelfelipeac.improov.features.profile.data.DataDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DataDataSourceTest {

    @Mock
    internal lateinit var mockHistoricDao: HistoricDao

    @Mock
    internal lateinit var mockItemDao: ItemDao

    @Mock
    internal lateinit var mockGoalDao: GoalDao

    @Mock
    internal lateinit var mockGoalDataModelMapper: GoalDataModelMapper

    private lateinit var dataDataSource: DataDataSource

    @Before
    fun setup() {
        dataDataSource =
            DataDataSource(mockGoalDao, mockItemDao, mockHistoricDao, mockGoalDataModelMapper)
    }

    @Test
    fun `GIVEN is successful WHEN generateData is called THEN the save method must be called 3 times`() {
        runBlocking {
            dataDataSource.generateData()

            verify(mockGoalDao, times(3)).save(mockGoalDataModelMapper.mapReverse(createGoal()))
        }
    }

    @Test
    fun `GIVEN is successful WHEN clearData is called THEN the delete methods must be called`() {
        runBlocking {
            given(mockGoalDao.getAll()).willReturn(listOf(createGoalDataModel()))
            given(mockItemDao.getAll()).willReturn(
                listOf(
                    createItemDataModel(),
                    createItemDataModel()
                )
            )
            given(mockHistoricDao.getAll()).willReturn(
                listOf(
                    createHistoricDataModel(),
                    createHistoricDataModel(),
                    createHistoricDataModel()
                )
            )

            dataDataSource.clearData()

            verify(mockGoalDao, times(1)).delete(createGoalDataModel())
            verify(mockItemDao, times(2)).delete(createItemDataModel())
            verify(mockHistoricDao, times(3)).delete(createHistoricDataModel())
        }
    }
}
