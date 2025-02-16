package ua.testwork.racing.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.testwork.racing.R
import ua.testwork.racing.presentation.screens.components.ScreenTitle

@Composable
fun PreparingView(
    modifier: Modifier,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            text = stringResource(R.string.prepare)
        )
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            text = text
        )

    }
}