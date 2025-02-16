package ua.testwork.racing.domain.repository

import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.domain.model.Racer

interface RacersRepository {
    suspend fun getRandomRacersByCount(count: Int): List<Racer>
    suspend fun updateRacersAfterRace(racersIds: List<Int>)
    fun getAllRacersStatistic(): Flow<List<Racer>>
}