package com.rafaelfelipeac.improov.features.goal.domain.repository

import com.rafaelfelipeac.improov.features.goal.domain.model.Historic

interface HistoricRepository {

    suspend fun getHistorical(): List<Historic>

    suspend fun getHistoric(historicId: Long): Historic

    suspend fun save(historic: Historic): Long

    suspend fun delete(historic: Historic)

}
