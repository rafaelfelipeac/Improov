package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.features.goal.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.model.HistoricDataModelMapper
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoricRepositoryImpl @Inject constructor(
    private val historicDAO: HistoricDAO,
    private val historicDataModelMapper: HistoricDataModelMapper
) : HistoricRepository {

    override suspend fun getHistorics(): List<Historic> {
        return withContext(Dispatchers.IO) {
            historicDAO.getAll()
                .map { historicDataModelMapper.map(it) }
        }
    }

    override suspend fun getHistoric(historicId: Long): Historic {
        return withContext(Dispatchers.IO) {
            historicDAO.get(historicId)
                .let { historicDataModelMapper.map(it) }
        }
    }

    override suspend fun save(historic: Historic): Long {
        return withContext(Dispatchers.IO) {
            historicDAO.save(historic
                .let { historicDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(historic: Historic) {
        return withContext(Dispatchers.IO) {
            historicDAO.delete(historic
                .let { historicDataModelMapper.mapReverse(it) })
        }
    }
}
