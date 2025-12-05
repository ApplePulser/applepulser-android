package com.shinythinking.applepulser_android.presentation.host.gameSetting

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import com.shinythinking.applepulser_android.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GameSettingViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val gameRepository: GameRepository,
    private val json: Json,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<GameSettingContract.State, GameSettingContract.SideEffect> {
    private val args = savedStateHandle.toRoute<Route.GameSettings>()
    private val roomId = args.roomId
    private val playerId = args.playerId

    override val container: Container<GameSettingContract.State, GameSettingContract.SideEffect> =
        container(GameSettingContract.State.ModeSelection())

    fun onIntent(intent: GameSettingContract.Intent) {
        when (intent) {
            // Step 1: Mode Selection
            is GameSettingContract.Intent.ModeSelected -> handleModeSelected(intent.mode)
            is GameSettingContract.Intent.ModeNextClicked -> handleModeNext()

            // Step 2: Settings Input
            is GameSettingContract.Intent.MinHeartRateChanged -> handleMinHeartRateChanged(intent.value)
            is GameSettingContract.Intent.MaxHeartRateChanged -> handleMaxHeartRateChanged(intent.value)
            is GameSettingContract.Intent.DurationChanged -> handleDurationChanged(intent.value)
            is GameSettingContract.Intent.SettingsNextClicked -> handleSettingsNext()

            // Step 3: Settings Check
            is GameSettingContract.Intent.LaunchClicked -> handleLaunch()

            // Common
            is GameSettingContract.Intent.BackClicked -> handleBack()
            is GameSettingContract.Intent.ErrorDismissed -> handleErrorDismissed()
        }
    }

    private fun handleModeSelected(mode: GameMode) = intent {
        reduce {
            when (state) {
                is GameSettingContract.State.ModeSelection -> {
                    (state as GameSettingContract.State.ModeSelection).copy(
                        selectedMode = mode
                    )
                }

                else -> state
            }
        }
    }

    private fun handleModeNext() = intent {
        val currentState = state as? GameSettingContract.State.ModeSelection

        if (currentState == null || !currentState.canProceed) {
            postSideEffect(GameSettingContract.SideEffect.ShowToast("Please select a game mode"))
            return@intent
        }

        reduce {
            GameSettingContract.State.SettingsInput(
                selectedMode = currentState.selectedMode!!
            )
        }
    }

    private fun handleMinHeartRateChanged(value: Int) = intent {
        reduce {
            when (state) {
                is GameSettingContract.State.SettingsInput -> {
                    (state as GameSettingContract.State.SettingsInput).copy(
                        minHeartRate = value.coerceIn(60, 180)
                    )
                }

                else -> state
            }
        }
    }

    private fun handleMaxHeartRateChanged(value: Int) = intent {
        reduce {
            when (state) {
                is GameSettingContract.State.SettingsInput -> {
                    (state as GameSettingContract.State.SettingsInput).copy(
                        maxHeartRate = value.coerceIn(80, 200)
                    )
                }

                else -> state
            }
        }
    }

    private fun handleDurationChanged(value: Int) = intent {
        reduce {
            when (state) {
                is GameSettingContract.State.SettingsInput -> {
                    (state as GameSettingContract.State.SettingsInput).copy(
                        duration = value.coerceIn(1, 15)
                    )
                }

                else -> state
            }
        }
    }

    private fun handleSettingsNext() = intent {
        val currentState = state as? GameSettingContract.State.SettingsInput

        if (currentState == null || !currentState.canProceed) {
            postSideEffect(GameSettingContract.SideEffect.ShowToast("Please check your settings"))
            return@intent
        }

        reduce {
            GameSettingContract.State.SettingsCheck(
                selectedMode = currentState.selectedMode,
                minHeartRate = currentState.minHeartRate,
                maxHeartRate = currentState.maxHeartRate,
                duration = currentState.duration,
            )
        }
    }

    private fun handleLaunch() = intent {
        val currentState = state as? GameSettingContract.State.SettingsCheck

        if (currentState == null) {
            return@intent
        }

        reduce { GameSettingContract.State.Launching }

        try {
            val roomInfo = roomRepository.getRoomInfo(roomId)
            Log.d(
                "GameSettingViewModel",
                "Game started  successfully${roomInfo.players.map { it.name }}"
            )

            val response = roomRepository.startGame(
                playerId = playerId,
                roomId = roomId,
                gameSetting = GameSetting(
                    roomId = roomId,
                    gameMode = currentState.selectedMode,
                    bpmMin = currentState.minHeartRate,
                    bpmMax = currentState.maxHeartRate,
                    players = roomInfo.players,
                    timeLimit = currentState.duration * 60
                )
            ).copy(
                players = roomInfo.players
            )
            Log.d("GameSettingViewModel", "before, Game started  successfully")
            postSideEffect(
                GameSettingContract.SideEffect.NavigateToGamePlay(
                    playerId = playerId,
                    roomId = roomId,
                    settingJson = json.encodeToString(response),
                    deviceAddress = "", //todo
                )
            )
            Log.d("GameSettingViewModel", "after, Game started  successfully")
        } catch (e: Exception) {
            Log.e("GameSettingViewModel", "CRASH in handleLaunch", e)
            reduce {
                GameSettingContract.State.Error(
                    message = e.message ?: "Failed to start game",
                    previousState = currentState
                )
            }
        }
    }

    private fun handleBack() = intent {
        when (state) {
            is GameSettingContract.State.ModeSelection -> {
                postSideEffect(GameSettingContract.SideEffect.NavigateBack)
            }

            is GameSettingContract.State.SettingsInput -> {
                val currentState = state as GameSettingContract.State.SettingsInput
                reduce {
                    GameSettingContract.State.ModeSelection(
                        selectedMode = currentState.selectedMode
                    )
                }
            }

            is GameSettingContract.State.SettingsCheck -> {
                val currentState = state as GameSettingContract.State.SettingsCheck
                reduce {
                    GameSettingContract.State.SettingsInput(
                        selectedMode = currentState.selectedMode,
                        minHeartRate = currentState.minHeartRate,
                        maxHeartRate = currentState.maxHeartRate,
                        duration = currentState.duration,
                    )
                }
            }

            is GameSettingContract.State.Error -> {
                val errorState = state as GameSettingContract.State.Error
                reduce { errorState.previousState ?: GameSettingContract.State.ModeSelection() }
            }

            else -> {
                postSideEffect(GameSettingContract.SideEffect.NavigateBack)
            }
        }
    }

    private fun handleErrorDismissed() = intent {
        val currentState = state as? GameSettingContract.State.Error
        reduce { currentState?.previousState ?: GameSettingContract.State.ModeSelection() }
    }
}