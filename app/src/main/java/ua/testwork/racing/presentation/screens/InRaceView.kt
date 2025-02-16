package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testwork.racing.R
import ua.testwork.racing.presentation.RacerInfo
import ua.testwork.racing.presentation.screens.components.ScreenTitle
import ua.testwork.racing.presentation.theme.RacingTheme


@Composable
fun InRaceView(
    modifier: Modifier,
    racers: List<RacerInfo>
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.racing)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(racers, key = { it.racerId }) { item ->
                RacerProgressView(
                    modifier = Modifier.animateItem(),
                    name = item.name,
                    progress = item.progress
                )
            }
        }
    }
}

@Composable
fun RacerProgressView(
    modifier: Modifier,
    name: String,
    progress: Float
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            text = name,
            maxLines = 1,
            fontSize = 18.sp
        )
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(24.dp)
                .weight(0.8f),
            progress = { progress },
            color = Color.Green,
            trackColor = Color.Gray
        )
    }

}

@Preview
@Composable
fun InRaceView_Preview() {
    RacingTheme {
        InRaceView(
            modifier = Modifier.fillMaxSize(),
            racers = listOf(
                RacerInfo(
                    1, "Racer1", 0.02f
                ),
                RacerInfo(
                    2, "Racer2", 0.2f
                ),
                RacerInfo(
                    3, "Racer3", 0.3f
                ),
                RacerInfo(
                    4, "Racer4", 0.4f
                ),
                RacerInfo(
                    5, "Racer5", 0.5f
                )
            )
        )
    }
}