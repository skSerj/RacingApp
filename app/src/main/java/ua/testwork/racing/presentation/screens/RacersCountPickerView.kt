package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.testwork.racing.R
import ua.testwork.racing.presentation.screens.components.BottomButton
import ua.testwork.racing.presentation.screens.components.ScreenTitle
import ua.testwork.racing.presentation.theme.RacingTheme

@Composable
fun RacersCountPickerView(
    modifier: Modifier,
    onCountPick: (Int) -> Unit
) {
    val clickAction by rememberUpdatedState(onCountPick)
    var currentCount by rememberSaveable { mutableIntStateOf(5) }

    val isDecrementAvailable by remember(currentCount) {
        derivedStateOf { currentCount > 4 }
    }
    val isIncrementAvailable by remember(currentCount) {
        derivedStateOf { currentCount <= 8 }
    }

    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            text = stringResource(R.string.choose_count_title)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PickerButton(
                "-",
                isEnabled = isDecrementAvailable,
                onClick = { currentCount-- }
            )
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "$currentCount",
                fontSize = 92.sp
            )
            PickerButton(
                "+",
                isEnabled = isIncrementAvailable,
                onClick = { currentCount++ }
            )

        }
        BottomButton(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            text = stringResource(R.string.start)
        ) { clickAction(currentCount) }
    }


}

@Composable
fun PickerButton(
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val onClickAction by rememberUpdatedState(onClick)
    Button(
        onClick = { onClickAction() },
        enabled = isEnabled
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            maxLines = 1,
            fontSize = 24.sp
        )
    }
}

@Preview
@Composable
fun RacersCountPickerView_Preview() {
    RacingTheme {
        RacersCountPickerView(
            modifier = Modifier.fillMaxSize(),
            onCountPick = {}
        )
    }
}