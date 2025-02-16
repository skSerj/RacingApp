package ua.testwork.racing.data

import ua.testwork.racing.data.local.entity.RacerEntity
import ua.testwork.racing.domain.model.Racer

object Mapper {

    fun RacerEntity.mapRacerEntityToRacer() = Racer(racerId, name, numOfWin)

    fun Racer.mapToRacerEntity() = RacerEntity(racerId, name, numOfWin)

}