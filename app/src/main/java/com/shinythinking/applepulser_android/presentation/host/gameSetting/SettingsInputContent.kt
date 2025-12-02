package com.shinythinking.applepulser_android.presentation.host.gameSetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APSettingSlider
import com.shinythinking.applepulser_android.presentation.base.component.TextWithSideBar
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun SettingsInputContent(
    selectedMode: GameMode,
    minHeartRate: Int,
    maxHeartRate: Int,
    duration: Int,
    onMinHeartRateChanged: (Int) -> Unit,
    onMaxHeartRateChanged: (Int) -> Unit,
    onDurationChanged: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(
        modifier = modifier,
        appleExist = false,
        backExist = true,
        buttonExist = true,
        buttonText = "Next",
        isClicked = false,
        onBackButtonClicked = onBackClicked,
        onButtonClick = onNextClicked
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(paddingValues),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextWithSideBar(
                title = "Set your target heart rate range\n(min ~ max bpm)\nto maintain during your workout",
                description = "",
                onlyTitle = true
            )

            Spacer(modifier = Modifier.height(48.dp))

            APSettingSlider(
                label = "min",
                value = minHeartRate,
                range = 60f..180f,
                onValueChange = { onMinHeartRateChanged(it.toInt()) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            APSettingSlider(
                label = "max",
                value = maxHeartRate,
                range = 80f..200f,
                onValueChange = { onMaxHeartRateChanged(it.toInt()) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            APSettingSlider(
                label = "min",
                value = duration,
                range = 1f..60f,
                unit = "min",
                onValueChange = { onDurationChanged(it.toInt()) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsInputPreview() {
    ApplepulserTheme {
        APBackground {
            SettingsInputContent(
                selectedMode = GameMode.STEADY_BEAT,
                minHeartRate = 120,
                maxHeartRate = 140,
                duration = 5,
                onMinHeartRateChanged = {},
                onMaxHeartRateChanged = {},
                onDurationChanged = {},
                onNextClicked = {},
                onBackClicked = {}
            )
        }
    }
}