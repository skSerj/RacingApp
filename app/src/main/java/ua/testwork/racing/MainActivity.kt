package ua.testwork.racing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ua.testwork.racing.presentation.RaceScreenViewModel
import ua.testwork.racing.presentation.theme.RacingTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.testwork.racing.presentation.screens.RacersCountPickerView
import ua.testwork.racing.presentation.RacingUiEvent
import ua.testwork.racing.presentation.RacingUiState
import ua.testwork.racing.presentation.screens.InRaceView
import ua.testwork.racing.presentation.screens.PreparingView
import ua.testwork.racing.presentation.screens.StatisticView
import ua.testwork.racing.presentation.screens.WinnersView

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RacingTheme { Racing() }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Racing(
    viewModel: RaceScreenViewModel = viewModel()
) {
    val navigator = rememberSupportingPaneScaffoldNavigator()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val racingState by viewModel.racingState.collectAsState()
    val statistics by viewModel.statistics.collectAsState()

    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        mainPane = {
            AnimatedPane(modifier = Modifier.safeContentPadding()) {
                when (val state = racingState) {
                    is RacingUiState.InitialState -> {
                        RacersCountPickerView(
                            modifier = Modifier.fillMaxSize(),
                            onCountPick = {
                                viewModel.onEvent(
                                    RacingUiEvent.onChooseNumOfRacers(
                                        it
                                    )
                                )
                            }
                        )
                    }

                    is RacingUiState.Preparing -> {
                        PreparingView(
                            modifier = Modifier.fillMaxSize(),
                            text = state.timeToStart.toString()
                        )
                    }

                    is RacingUiState.InRace -> {
                        InRaceView(
                            modifier = Modifier.fillMaxSize(),
                            racers = state.racers
                        )
                    }

                    is RacingUiState.Finish -> {
                        WinnersView(
                            modifier = Modifier.fillMaxSize(),
                            winners = state.winners,
                            isNeedShowStatsBtn = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden
                        ) {
                            navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane(modifier = Modifier.safeContentPadding()) {
                StatisticView(
                    modifier = Modifier.fillMaxSize(),
                    racers = statistics
                )
            }
        },
    )

}