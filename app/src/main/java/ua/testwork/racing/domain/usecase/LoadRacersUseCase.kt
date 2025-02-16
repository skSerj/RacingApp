package ua.testwork.racing.domain.usecase

import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.domain.repository.RacersRepository
import javax.inject.Inject

class LoadRacersUseCase @Inject constructor(
    private val racersRepository: RacersRepository
) {

    suspend operator fun invoke(numOfRacer: Int): List<Racer> =
        racersRepository.getRandomRacersByCount(numOfRacer)

    fun getRacersStatisticFlow() = racersRepository.getAllRacersStatistic()
}