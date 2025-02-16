package ua.testwork.racing.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BottomButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth(0.5f),
        onClick = onClick,
        enabled = isEnabled
    ) {
        Text(
            modifier = Modifier.wrapContentSize(Alignment.Center),
            text = text,
            fontSize = 48.sp
        )
    }
}

@Composable
fun ScreenTitle(
    modifier: Modifier,
    text: String
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        fontSize = 48.sp,
        textAlign = TextAlign.Center,
        lineHeight = 55.sp
    )
}