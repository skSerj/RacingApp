package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testwork.racing.R
import ua.testwork.racing.domain.model.Racer
import ua.testwork.racing.presentation.screens.components.ScreenTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticView(
    modifier: Modifier,
    racers: List<Racer>
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .border(1.dp,Color.LightGray),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.statistics_title)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stickyHeader {
                StatisticLine(
                    name = stringResource(R.string.name_title),
                    numOfFinishing = stringResource(R.string.finish_count)
                )
            }
            items(racers, key = { it.racerId }) { racer ->
                StatisticLine(
                    name = racer.name,
                    numOfFinishing = racer.numOfWin.toString()
                )
            }
        }
    }

}

@Composable
fun StatisticLine(
    name: String,
    numOfFinishing: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.DarkGray),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = Color.Gray,
            thickness = 2.dp
        )

        Text(
            modifier = Modifier.weight(1f),
            text = numOfFinishing,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}