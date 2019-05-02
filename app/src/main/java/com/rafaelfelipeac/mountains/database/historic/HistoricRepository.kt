package com.rafaelfelipeac.mountains.database.historic

import com.rafaelfelipeac.mountains.models.Historic
import javax.inject.Inject

class HistoricRepository @Inject constructor(private val historicDAO: HistoricDAO) {

    fun getHistorical(): List<Historic> {
        return historicDAO.getAll()
    }

    fun insert(historic: Historic) {
        return historicDAO.insert(historic)
    }

    fun update(historic: Historic) {
        return historicDAO.update(historic)
    }

    fun delete(historic: Historic) {
        return historicDAO.delete(historic)
    }
}