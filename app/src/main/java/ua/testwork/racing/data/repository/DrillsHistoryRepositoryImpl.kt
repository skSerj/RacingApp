package ua.testwork.racing.data.repository

import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.data.Mapper.mapToLocalModel
import ua.testwork.racing.data.Mapper.toEntity
import ua.testwork.racing.data.local.dao.HistoriesDao
import ua.testwork.racing.domain.model.StageShootingHistoryModel
import ua.testwork.racing.domain.repository.DrillsHistoryRepository
import javax.inject.Inject

class DrillsHistoryRepositoryImpl @Inject constructor(
    private val historiesDao: HistoriesDao
) : DrillsHistoryRepository {
    override suspend fun insertHistory(history: StageShootingHistoryModel) {
        historiesDao.insertHistory(history.toEntity())
    }

    override suspend fun insertHistories(histories: List<StageShootingHistoryModel>) {
        historiesDao.insertHistories(histories.map { it.toEntity() })
    }

    override suspend fun getHistoryByInternalId(id: String): StageShootingHistoryModel {
        return historiesDao.getHistoryByInternalId(id).mapToLocalModel()
    }

    override suspend fun getAllHistories(): List<StageShootingHistoryModel> {
        return historiesDao.getAllHistories().map { it.mapToLocalModel() }
    }

    override fun getHistoriesCount(): Flow<Int> = historiesDao.observeCount()

    override suspend fun deleteAll() {
        historiesDao.deleteAll()
    }

}