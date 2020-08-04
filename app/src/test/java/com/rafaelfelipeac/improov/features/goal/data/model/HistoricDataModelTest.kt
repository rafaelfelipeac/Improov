package com.rafaelfelipeac.improov.features.goal.data.model

import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoricDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HistoricDataModelTest {

    private val mapper = HistoricDataModelMapper()

    @Test
    fun `GIVEN historicDataModel WHEN map is called THEN a historic is returned`() {
        // given
        val historicDataModel = createHistoricDataModel()

        // when
        val historic = historicDataModel.let { mapper.map(historicDataModel) }

        // then
        historic equalTo createHistoric()
    }

    @Test
    fun `GIVEN historicDataModel WHEN map is called THEN a historic with the same values is returned`() {
        // given
        val historicDataModel = createHistoricDataModel(historicId = 123, value = 5f)

        // when
        val historic = historicDataModel.let { mapper.map(historicDataModel) }

        // then
        historic equalTo createHistoric(historicId = 123, value = 5f)
    }

    @Test
    fun `GIVEN historic WHEN mapReverse is called THEN historicDataModel is returned`() {
        // given
        val historic = createHistoric()

        // when
        val historicDataModel = historic.let { mapper.mapReverse(historic) }

        // then
        historicDataModel equalTo createHistoricDataModel()
    }

    @Test
    fun `GIVEN historic WHEN mapReverse is called THEN historicDataModel with the same values is returned`() {
        // given
        val historic = createHistoric(historicId = 123, value = 10f)

        // when
        val historicDataModel = historic.let { mapper.mapReverse(historic) }

        // then
        historicDataModel equalTo createHistoricDataModel(
            historicId = 123,
            value = 10f
        )
    }
}
