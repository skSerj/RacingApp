package ua.testwork.racing.domain.usecase

import ua.testwork.racing.domain.repository.RacersRepository
import javax.inject.Inject

class UpdateRacersUseCase @Inject constructor(
    private val racersRepository: RacersRepository
) {

    suspend operator fun invoke(racersId: List<Int>) {
        racersRepository.updateRacersAfterRace(racersId)
    }
}