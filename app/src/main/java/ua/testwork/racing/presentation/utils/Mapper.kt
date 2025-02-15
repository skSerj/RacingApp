package ua.testwork.racing.presentation.utils

import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.presentation.screens.main.RacerInfo

fun List<Racer>.mapRacerListToRacerInfoList() =
    map { racer ->
        RacerInfo(
            racerId = racer.racerId,
            name = racer.name,
        )
    }