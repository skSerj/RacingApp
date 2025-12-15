package ua.testwork.racing.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.domain.model.StageShootingHistoryModel
import ua.testwork.racing.domain.repository.DrillsHistoryRepository
import ua.testwork.racing.domain.usecase.LoadRacersUseCase
import ua.testwork.racing.domain.usecase.UpdateRacersUseCase
import ua.testwork.racing.presentation.screens.DrillsHistoriesEvent
import ua.testwork.racing.presentation.screens.DrillsHistoriesState
import ua.testwork.racing.presentation.utils.ActorMessage
import ua.testwork.racing.presentation.utils.completionActor
import ua.testwork.racing.presentation.utils.getRandomFloat
import ua.testwork.racing.presentation.utils.mapRacerListToRacerInfoList
import java.util.Date
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.random.Random

private const val VM_EXCEPTION_HANDLER_TAG = "coroutine_exception"
private const val RACER_PROGRESS_TAG = "racer_progress"

private const val TARGET_WINNERS_IN_RACE = 3

@HiltViewModel
class RaceScreenViewModel @Inject constructor(
    private val loadRacersUseCase: LoadRacersUseCase,
    private val updateRacersUseCase: UpdateRacersUseCase,
    private val drillsHistoriesRepository: DrillsHistoryRepository
) : ViewModel() {

    private val _racingState: MutableStateFlow<RacingUiState> =
        MutableStateFlow(RacingUiState.InitialState)
    internal val racingState: StateFlow<RacingUiState> = _racingState.asStateFlow()

    private val _statistics: MutableStateFlow<List<Racer>> = MutableStateFlow(listOf())
    internal val statistics = _statistics.asStateFlow()

    private val _histories: MutableStateFlow<DrillsHistoriesState> =
        MutableStateFlow(DrillsHistoriesState())
    internal val histories = _histories.asStateFlow()


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(VM_EXCEPTION_HANDLER_TAG, throwable.message, throwable)
    }

    internal fun onEvent(event: RacingUiEvent) {
        when (event) {
            is RacingUiEvent.onChooseNumOfRacers -> prepareRacers(event.num)
        }
    }

    init {
        collectStatistics()
        observeHistoriesCount()
    }

    private fun collectStatistics() {
        viewModelScope.launch {
            loadRacersUseCase.getRacersStatisticFlow()
                .collect { racers ->
                    _statistics.value = racers
                }
        }
    }

    private fun prepareRacers(numOfRacers: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            prepareAndStartRace(loadRacersUseCase.invoke(numOfRacers).mapRacerListToRacerInfoList())
        }
    }

    private suspend fun prepareAndStartRace(racers: List<RacerInfo>) {
        prepare()
        startRace(racers)
    }

    private suspend fun prepare() {
        for (sec in 3 downTo 1) {
            _racingState.update { RacingUiState.Preparing(sec) }
            delay(1_000L)
        }
    }

    private suspend fun startRace(racers: List<RacerInfo>) = coroutineScope {
        val completionActor = completionActor(TARGET_WINNERS_IN_RACE)
        _racingState.update { RacingUiState.InRace(racers) }
        val currentRacePosition = AtomicInteger(0)
        val jobs = racers.map { originRacer ->
            launch {
                var updatedProgress = 0f
                while (updatedProgress < 1.0f) {
                    updatedProgress += getRandomFloat(0.01f, 0.15f).coerceAtMost(1.0f)
                    delay(Random.nextLong(500L, 1_500L))

                    _racingState.update { state ->
                        when (state) {
                            is RacingUiState.InRace -> {
                                val updatedRacers = state.racers.map { racer ->
                                    if (racer.racerId == originRacer.racerId) {
                                        Log.i(
                                            RACER_PROGRESS_TAG,
                                            "racer:${racer.name} progress: $updatedProgress"
                                        )
                                        racer.copy(
                                            progress = updatedProgress,
                                            isFinished = updatedProgress >= 1.0f,
                                            finishedPosition = if (updatedProgress >= 1.0f) currentRacePosition.incrementAndGet() else racer.finishedPosition
                                        )
                                    } else racer
                                }.sortedByDescending { it.progress }
                                RacingUiState.InRace(updatedRacers)
                            }

                            else -> {
                                state
                            }
                        }
                    }
                }
                completionActor.send(ActorMessage.ProcessCompleted)
            }
        }

        completionActor.invokeOnClose {
            Log.i(RACER_PROGRESS_TAG, "Actor closed, stopping remaining racers")
            cancelRace()
        }

        jobs.joinAll()
    }

    private fun cancelRace() {
        _racingState.getAndUpdate { state ->
            when (state) {
                is RacingUiState.InRace -> {
                    Log.e("FINISH", state.racers.toString())
                    val winners =
                        state.racers.filter { it.isFinished }.sortedBy { it.finishedPosition }
                    saveRaceStatistic(winners)
                    RacingUiState.Finish(winners)
                }

                else -> {
                    throw IllegalStateException("wrong state>>>Expected ${RacingUiState.InRace::class.java.name}, but actual: ${state::class.java.name}")
                }
            }
        }
    }

    private fun saveRaceStatistic(winners: List<RacerInfo>) {
        viewModelScope.launch {
            updateRacersUseCase.invoke(winners.map { it.racerId })
        }
    }

    internal fun observeHistoriesEvent(event: DrillsHistoriesEvent) {
        when (event) {
            DrillsHistoriesEvent.startObserve -> {
                observeHistoriesCount()
            }

            DrillsHistoriesEvent.onStartInsert -> {
                insertHistories()
            }

            DrillsHistoriesEvent.onStartGetting -> {
                getAllHistories()
            }

            DrillsHistoriesEvent.onDeleteAll -> {
                removeAllHistories()
            }
        }

    }

    private fun observeHistoriesCount() {
        viewModelScope.launch(Dispatchers.IO) {
            drillsHistoriesRepository.getHistoriesCount().collect { count ->
                _histories.update {
                    it.copy(
                        historiesInDb = count
                    )
                }
            }
        }
    }

    private fun insertHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            //generate n of basic histories
            val numOfHistories = 100
            val list = mutableListOf<StageShootingHistoryModel>()
            for (i in 1..numOfHistories) {
                list.add(StageShootingHistoryModel(UUID.randomUUID().toString()))
            }
            val startMillis = System.currentTimeMillis()
            _histories.update { it.copy(startInsertHistoriesDate = Date(startMillis)) }
            drillsHistoriesRepository.insertHistories(list)
            val endMillis = System.currentTimeMillis()
            val duration = endMillis - startMillis
            _histories.update {
                it.copy(
                    insertHistoriesCompleteDate = Date(endMillis),
                    insertingDurationInMillis = duration
                )
            }
        }
    }

    private fun getAllHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            val startMillis = System.currentTimeMillis()
            _histories.update { it.copy(startGettingHistoriesDate = Date(startMillis)) }
            val lists = drillsHistoriesRepository.getAllHistories()
            val endMillis = System.currentTimeMillis()
            val duration = endMillis - startMillis
            _histories.update {
                it.copy(
                    getHistoriesCompleteDate = Date(endMillis),
                    gettingHistoriesDurationInMillis = duration
                )

            }
            println("get ${lists.size} histories from db")

        }
    }

    private fun removeAllHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            drillsHistoriesRepository.deleteAll()
            _histories.update {
                DrillsHistoriesState()
            }
        }
    }
}