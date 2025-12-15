package ua.testwork.racing.data.repository

import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.data.Mapper.mapToLocalModel
import ua.testwork.racing.data.Mapper.toEntity
import ua.testwork.racing.data.local.dao.HistoriesDao
import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity
import ua.testwork.racing.domain.model.StageShootingHistoryModel
import ua.testwork.racing.domain.repository.DrillsHistoryRepository
import javax.inject.Inject

class DrillsHistoryRepositoryImpl @Inject constructor(
    private val historiesDao: HistoriesDao
) : DrillsHistoryRepository {
    override suspend fun insertHistory(history: StageShootingHistoryModel) {
        historiesDao.insertHistory(history.toEntity())
    }

    override suspend fun insertHistories(histories: List<HistoryShootPracticeEntity>) {
        historiesDao.insertHistories(histories)
    }

    override suspend fun getHistoryByInternalId(id: String): StageShootingHistoryModel {
        return historiesDao.getHistoryByInternalId(id).mapToLocalModel()
    }

    override suspend fun getAllHistories(): List<HistoryShootPracticeEntity> {
        return historiesDao.getAllHistories()
    }

    override fun getHistoriesCount(): Flow<Int> = historiesDao.observeCount()

    override suspend fun deleteAll() {
        historiesDao.deleteAll()
    }

}