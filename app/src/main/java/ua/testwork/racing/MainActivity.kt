package ua.testwork.racing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import ua.testwork.racing.presentation.screens.main.RaceScreenViewModel
import ua.testwork.racing.presentation.theme.RacingTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.testwork.racing.presentation.screens.main.RacingUiEvent
import ua.testwork.racing.presentation.screens.main.RacingUiState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RacingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: RaceScreenViewModel = viewModel()
) {
    val racingState by viewModel.racingState.collectAsState()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        when (val state = racingState) {
            is RacingUiState.InitialState -> {
                Button(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopCenter),
                    onClick = { viewModel.onEvent(RacingUiEvent.onChooseNumOfRacers(5)) }
                ) {
                    Text("start with 5 racers")
                }
            }

            is RacingUiState.Preparing -> {
                Text(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center),
                    text = "racing start after: ${state.timeToStart}"
                )
            }

            is RacingUiState.InRace -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    items(state.racers, key = { it.racerId }) { item ->
                        LinearProgressIndicator(
                            modifier = Modifier.fillParentMaxWidth(0.5f),
                            progress = {
                                item.progress
                            },
                            color = Color.Green,
                            trackColor = Color.Gray
                        )
                    }
                }
            }

            is RacingUiState.Finish -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(state.winners.size){ place ->
                        Text("place:${place+1} :::> ${state.winners[place].name}")
                    }
                }

            }
        }

    }
}