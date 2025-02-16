package ua.testwork.racing.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.testwork.racing.data.Mapper.mapRacerEntityToRacer
import ua.testwork.racing.data.local.dao.RacersDao
import ua.testwork.racing.data.local.entity.RacerEntity
import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.domain.repository.RacersRepository
import javax.inject.Inject

class RacersRepositoryImpl @Inject constructor(
    private val racersDao: RacersDao
) : RacersRepository {
    override suspend fun getRandomRacersByCount(count: Int): List<Racer> {
        val racersCountInDb = racersDao.getRacerCount()
        if( racersCountInDb < count){
            val newRacers = mutableListOf<RacerEntity>()
            for (num in racersCountInDb until racersCountInDb+(count - racersCountInDb)){
                newRacers.add(
                    RacerEntity(
                        name = "Racer$num",
                        numOfWin = 0
                    )
                )
            }
            racersDao.updateRacers(newRacers)
        }
        return racersDao.getRandomRacers(count)
            .map { it.mapRacerEntityToRacer() }
    }

    override suspend fun updateRacersAfterRace(racersIds: List<Int>) { racersDao.incrementScores(racersIds)}

    override fun getAllRacersStatistic(): Flow<List<Racer>> {
        return racersDao.getAllRacers()
            .map { it.map { racerEntity -> racerEntity.mapRacerEntityToRacer() } }
    }
}