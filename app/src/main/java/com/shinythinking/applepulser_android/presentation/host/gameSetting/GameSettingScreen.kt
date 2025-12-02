package com.shinythinking.applepulser_android.presentation.host.gameSetting

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GameSettingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGamePlay: () -> Unit,
    viewModel: GameSettingViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GameSettingContract.SideEffect.NavigateToGamePlay -> onNavigateToGamePlay()
            is GameSettingContract.SideEffect.NavigateBack -> onNavigateBack()
            is GameSettingContract.SideEffect.ShowToast -> {
                // todo
            }
        }
    }

    APBackground {
        GameSettingContent(
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
fun GameSettingContent(
    state: GameSettingContract.State,
    onIntent: (GameSettingContract.Intent) -> Unit
) {
    AnimatedContent(
        targetState = state,
        label = "step_transition"
    ) { currentState ->
        when (currentState) {
            is GameSettingContract.State.ModeSelection -> {
                ModeSelectionContent(
                    selectedMode = currentState.selectedMode,
                    onModeSelected = { onIntent(GameSettingContract.Intent.ModeSelected(it)) },
                    onNextClicked = { onIntent(GameSettingContract.Intent.ModeNextClicked) },
                    onBackClicked = { onIntent(GameSettingContract.Intent.BackClicked) }
                )
            }

            is GameSettingContract.State.SettingsInput -> {
                SettingsInputContent(
                    selectedMode = currentState.selectedMode,
                    minHeartRate = currentState.minHeartRate,
                    maxHeartRate = currentState.maxHeartRate,
                    duration = currentState.duration,
                    onMinHeartRateChanged = {
                        onIntent(
                            GameSettingContract.Intent.MinHeartRateChanged(
                                it
                            )
                        )
                    },
                    onMaxHeartRateChanged = {
                        onIntent(
                            GameSettingContract.Intent.MaxHeartRateChanged(
                                it
                            )
                        )
                    },
                    onDurationChanged = { onIntent(GameSettingContract.Intent.DurationChanged(it)) },
                    onNextClicked = { onIntent(GameSettingContract.Intent.SettingsNextClicked) },
                    onBackClicked = { onIntent(GameSettingContract.Intent.BackClicked) }
                )
            }

            is GameSettingContract.State.SettingsCheck -> {
                SettingsCheckContent(
                    selectedMode = currentState.selectedMode,
                    minHeartRate = currentState.minHeartRate,
                    maxHeartRate = currentState.maxHeartRate,
                    duration = currentState.duration,
                    onLaunchClicked = { onIntent(GameSettingContract.Intent.LaunchClicked) },
                    onBackClicked = { onIntent(GameSettingContract.Intent.BackClicked) }
                )
            }

            is GameSettingContract.State.Launching -> {
                LaunchingScreen()
            }

            is GameSettingContract.State.Error -> {
                // todo
            }
        }
    }
}

@Composable
fun LaunchingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Launching game...",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}
