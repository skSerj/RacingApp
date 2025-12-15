package ua.testwork.racing.data

import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity
import ua.testwork.racing.data.local.entity.RacerEntity
import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.domain.model.StageShootingHistoryModel

object Mapper {

    fun RacerEntity.mapRacerEntityToRacer() = Racer(racerId, name, numOfWin)

    fun Racer.mapToRacerEntity() = RacerEntity(racerId, name, numOfWin)


    fun HistoryShootPracticeEntity.mapToLocalModel() =
        StageShootingHistoryModel(
            internalId = internalId,
            remoteId = remoteId,
            shootPracticsID = shootPracticsID,
            objectType = objectType,
            shootPracticsName = shootPracticsName,
            shootPracticsTimeLimit = shootPracticsTimeLimit,
            shootPracticsFirstSignalDelay = shootPracticsFirstSignalDelay,
            shootPracticsShootLimit = shootPracticsShootLimit,
            shootPracticsBestSplit = shootPracticsBestSplit,
            shootPracticsAvgEfficiency = shootPracticsAvgEfficiency,
            shootPracticsDistance = shootPracticsDistance,
            shootPracticsDescription = shootPracticsDescription,
            date = date,
            dayNumber = dayNumber,
            practiceType = practiceType,
            video = video,
            videoLink = videoLink,
            hitFactor = hitFactor,
            shootPracticsTargets = shootPracticsTargets,
            sgTimerSession = sgTimerSession,
            timerType = timerType,
            stageType = stageType,
            photos = photos,
            isWithPulse = isWithPulse,
            isConnectionWasLost = isConnectionWasLost
        )

    fun StageShootingHistoryModel.toEntity() =
        HistoryShootPracticeEntity(
            internalId = internalId,
            remoteId = remoteId,
            shootPracticsID = shootPracticsID,
            objectType = objectType,
            shootPracticsName = shootPracticsName,
            shootPracticsTimeLimit = shootPracticsTimeLimit,
            shootPracticsFirstSignalDelay = shootPracticsFirstSignalDelay,
            shootPracticsShootLimit = shootPracticsShootLimit,
            shootPracticsBestSplit = shootPracticsBestSplit,
            shootPracticsAvgEfficiency = shootPracticsAvgEfficiency,
            shootPracticsDistance = shootPracticsDistance,
            shootPracticsDescription = shootPracticsDescription,
            date = date,
            dayNumber = dayNumber,
            practiceType = practiceType,
            video = video,
            videoLink = videoLink,
            hitFactor = hitFactor,
            shootPracticsTargets = shootPracticsTargets,
            sgTimerSession = sgTimerSession,
            timerType = timerType,
            stageType = stageType,
            photos = photos,
            isWithPulse = isWithPulse,
            isConnectionWasLost = isConnectionWasLost
        )
}