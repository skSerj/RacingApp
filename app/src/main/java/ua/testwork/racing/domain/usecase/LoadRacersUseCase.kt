package ua.testwork.racing.domain.usecase

import ua.testwork.racing.domain.model.Racer

class LoadRacersUseCase {

    suspend operator fun invoke(numOfRacer: Int): List<Racer> =
        List(
            size = numOfRacer,
            init = {index: Int ->
                Racer(
                    racerId = index,
                    name = "Racer$index",
                    numOfWin = 0
                )
            }
        )
}