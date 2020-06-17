package com.rafaelfelipeac.improov.features.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rafaelfelipeac.improov.core.TwoWayMapper
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import java.io.Serializable
import java.util.Date
import javax.inject.Inject

@Entity(tableName = "historic")
data class HistoricDataModel(
    @PrimaryKey(autoGenerate = true)
    var historicId: Long = 0,
    var goalId: Long,
    var value: Float,
    var date: Date?
) : Serializable

class HistoricDataModelMapper @Inject constructor() : TwoWayMapper<HistoricDataModel, Historic> {

    override fun map(param: HistoricDataModel): Historic = with(param) {
        Historic(
            historicId = historicId,
            goalId = goalId,
            value = value,
            date = date
        )
    }

    override fun mapReverse(param: Historic): HistoricDataModel = with(param) {
        HistoricDataModel(
            historicId = historicId,
            goalId = goalId,
            value = value,
            date = date
        )
    }
}
