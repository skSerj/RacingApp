package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Date

data class DrillsHistoriesState(
    val historiesInDb: Int = 0,
    val startInsertHistoriesDate: Date? = null,
    val insertHistoriesCompleteDate: Date? = null,
    val insertingDurationInMillis: Long? = null,
    val startGettingHistoriesDate: Date? = null,
    val getHistoriesCompleteDate: Date? = null,
    val gettingHistoriesDurationInMillis: Long? = null
)

interface DrillsHistoriesEvent {
    object startObserve: DrillsHistoriesEvent
    object onStartInsert: DrillsHistoriesEvent
    object onStartGetting: DrillsHistoriesEvent
    object onDeleteAll: DrillsHistoriesEvent
}

@Composable
fun DrillsHistoriesView(
    modifier: Modifier = Modifier,
    state: DrillsHistoriesState,
    onEvent: (DrillsHistoriesEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "histories in db: ${state.historiesInDb}",
            maxLines = 1,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
        )

        BanchmarkContent(
            onStart = {
                onEvent(DrillsHistoriesEvent.onStartGetting)
            },
            blockTitle = "getting histories",
            startTime = state.startGettingHistoriesDate,
            endTime = state.getHistoriesCompleteDate,
            durationInMillis = state.gettingHistoriesDurationInMillis
        )

        BanchmarkContent(
            onStart = {
                onEvent(DrillsHistoriesEvent.onStartInsert)
            },
            blockTitle = "insert histories",
            startTime = state.startInsertHistoriesDate,
            endTime = state.insertHistoriesCompleteDate,
            durationInMillis = state.insertingDurationInMillis
        )

        Button(
            onClick = {
                onEvent(DrillsHistoriesEvent.onDeleteAll)
            }
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "DELETE HISTORIES",
                maxLines = 1,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = Color.Yellow
            )
        }
    }
}

@Composable
private fun BanchmarkContent(
    onStart:() -> Unit,
    blockTitle: String,
    startTime: Date?,
    endTime: Date?,
    durationInMillis: Long?
) {
    Card(
        onClick = onStart
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = blockTitle,
            maxLines = 1,
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            fontStyle = FontStyle.Italic,
            color = Color.Black
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                ParametersLine(
                    title = "start",
                    value = startTime?.toString() ?: ""
                )
                ParametersLine(
                    title = "end",
                    value = endTime?.toString() ?: ""
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "duration: ${durationInMillis ?: ""} ms",
                maxLines = 1,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontStyle = FontStyle.Italic,
                color = Color.Green
            )

        }
    }
}

@Composable
private fun ParametersLine(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$title: ",
            maxLines = 1,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )
        Text(
            text = value,
            maxLines = 1,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )
    }
}