package ua.testwork.racing.presentation

import androidx.compose.runtime.Stable

sealed interface RacingUiState {
    object InitialState : RacingUiState
    data class Preparing(val timeToStart: Int) : RacingUiState
    data class InRace(val racers: List<RacerInfo>) : RacingUiState
    data class Finish(val winners: List<RacerInfo>) : RacingUiState
}

@Stable
data class RacerInfo(
    val racerId: Int,
    val name: String,
    val progress: Float = 0f,
    val isFinished: Boolean = false,
    val finishedPosition: Int? = null
)

sealed interface RacingUiEvent {
    data class onChooseNumOfRacers(val num: Int): RacingUiEvent
}