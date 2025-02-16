package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testwork.racing.R
import ua.testwork.racing.presentation.RacerInfo
import ua.testwork.racing.presentation.screens.components.BottomButton
import ua.testwork.racing.presentation.screens.components.ScreenTitle

@Composable
fun WinnersView(
    modifier: Modifier,
    isNeedShowStatsBtn: Boolean,
    winners: List<RacerInfo>,
    onShowStatistics: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            text = stringResource(R.string.winners)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(3) { index ->
                val racer = winners.first { it.finishedPosition == index + 1 }
                WinnerLine(
                    name = racer.name,
                    position = racer.finishedPosition ?: (index + 1)
                )
            }
        }

        if (isNeedShowStatsBtn) {
            BottomButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.statistics)
            ) { onShowStatistics() }
        }
    }
}

@Composable
fun WinnerLine(
    name: String,
    position: Int
) {
    val textColor by remember {
        mutableStateOf(
            when (position) {
                1 -> Color(0xFFFFD700)
                2 -> Color(0xFFC0C0C0)
                3 -> Color(0xFFCD7F32)
                else -> Color.Gray
            }
        )
    }

    val placeText by remember {
        mutableIntStateOf(
            when (position) {
                1 -> R.string.first_place
                2 -> R.string.second_place
                3 -> R.string.third_place
                else -> throw IllegalStateException("wrong place")
            }
        )
    }

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(placeText),
            fontSize = 30.sp,
            color = textColor
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = name,
            fontSize = 30.sp
        )
    }
}