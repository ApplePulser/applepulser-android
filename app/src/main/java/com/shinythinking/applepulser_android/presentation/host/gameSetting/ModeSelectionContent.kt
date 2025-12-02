package com.shinythinking.applepulser_android.presentation.host.gameSetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APBigButton
import com.shinythinking.applepulser_android.presentation.base.component.TextWithSideBar
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun ModeSelectionContent(
    selectedMode: GameMode?,
    onModeSelected: (GameMode) -> Unit,
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    APBackground(
        modifier = modifier,
        backExist = true,
        buttonExist = true,
        buttonText = "Next",
        onBackButtonClicked = onBackClicked,
        onButtonClick = onNextClicked
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Mode",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                APBigButton(
                    onClick = { onModeSelected(GameMode.STEADY_BEAT) },
                    text = "Steady\nBeat",
                    modifier = Modifier.weight(1f),
                    isClicked = selectedMode == GameMode.STEADY_BEAT
                )
                APBigButton(
                    onClick = { onModeSelected(GameMode.PULSE_RUSH) },
                    text = "Beat\nRush",
                    modifier = Modifier.weight(1f),
                    isClicked = selectedMode == GameMode.PULSE_RUSH
                )
            }
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextWithSideBar(
                    title = "Consistency is key",
                    description = "Hold your pulse in range\nto climb the leaderboard",
                    modifier = Modifier.weight(1f)
                )
                TextWithSideBar(
                    title = "Push your limits",
                    description = "Compete to hit \nthe highest heart rate",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ModeSelectionPreview() {

    ApplepulserTheme {
        APBackground {
            ModeSelectionContent(
                selectedMode = GameMode.STEADY_BEAT,
                onModeSelected = {},
                onNextClicked = {},
                onBackClicked = {}
            )
        }
    }
}
