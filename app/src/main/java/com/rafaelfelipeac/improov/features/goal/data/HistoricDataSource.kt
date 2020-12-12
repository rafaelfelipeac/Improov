package com.rafaelfelipeac.improov.features.goal.data

import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoricDataSource @Inject constructor(
    private val historicDao: HistoricDao,
    private val historicDataModelMapper: HistoricDataModelMapper
) : HistoricRepository {

    override suspend fun getHistorics(): List<Historic> {
        return withContext(Dispatchers.IO) {
            historicDao.getAll()
                .map { historicDataModelMapper.map(it) }
        }
    }

    override suspend fun getHistoric(historicId: Long): Historic {
        return withContext(Dispatchers.IO) {
            historicDao.get(historicId)
                .let { historicDataModelMapper.map(it) }
        }
    }

    override suspend fun save(historic: Historic): Long {
        return withContext(Dispatchers.IO) {
            historicDao.save(historic
                .let { historicDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(historic: Historic) {
        return withContext(Dispatchers.IO) {
            historicDao.delete(historic
                .let { historicDataModelMapper.mapReverse(it) })
        }
    }
}
