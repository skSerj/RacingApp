package ua.testwork.racing.domain.repository

import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity
import ua.testwork.racing.domain.model.StageShootingHistoryModel

interface DrillsHistoryRepository {
    suspend fun insertHistory(history: StageShootingHistoryModel)
    suspend fun insertHistories(histories: List<HistoryShootPracticeEntity>)

    suspend fun getHistoryByInternalId(id: String): StageShootingHistoryModel
    suspend fun getAllHistories(): List<HistoryShootPracticeEntity>

    fun getHistoriesCount() : Flow<Int>

    suspend fun deleteAll()
}