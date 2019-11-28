package com.rafaelfelipeac.improov.features.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.features.goal.data.HistoricDAO
import javax.inject.Inject

class HistoricRepository @Inject constructor(private val historicDAO: HistoricDAO) {

    fun getHistory(): LiveData<List<Historic>> {
        return historicDAO.getAll()
    }

    fun getHistoric(historicId: Long): LiveData<Historic> {
        return historicDAO.get(historicId)
    }

    fun save(historic: Historic): Long {
        return historicDAO.save(historic)
    }

    fun delete(historic: Historic) {
        return historicDAO.delete(historic)
    }
}
