package com.rafaelfelipeac.mountains.database.historic

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Historic
import javax.inject.Inject

class HistoricRepository @Inject constructor(private val historicDAO: HistoricDAO) {

    fun getHistorical(): LiveData<List<Historic>> {
        return historicDAO.getAll()
    }

    fun save(historic: Historic): Long {
        return historicDAO.save(historic)
    }

    fun delete(historic: Historic) {
        return historicDAO.delete(historic)
    }
}