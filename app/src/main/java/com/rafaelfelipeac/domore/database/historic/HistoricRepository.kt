package com.rafaelfelipeac.domore.database.historic

import com.rafaelfelipeac.domore.models.Historic
import javax.inject.Inject

class HistoricRepository @Inject constructor(private val historicDAO: HistoricDAO) {

    fun getItems(): List<Historic> {
        return historicDAO.getAll()
    }

    fun insert(historic: Historic) {
        return historicDAO.insert(historic)
    }
}