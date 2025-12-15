package com.shinythinking.applepulser_android.presentation.game

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shinythinking.applepulser_android.BuildConfig
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.GameStatus
import com.shinythinking.applepulser_android.domain.model.Limit
import com.shinythinking.applepulser_android.domain.model.event.GameEvent
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.HeartbeatRepository
import com.shinythinking.applepulser_android.domain.usecase.SyncHeartbeatUseCase
import com.shinythinking.applepulser_android.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SteadyBeatViewmodel @Inject constructor(
    private val gameRepository: GameRepository,
    private val heartbeatRepository: HeartbeatRepository,
    private val syncHeartbeatUseCase: SyncHeartbeatUseCase,
    private val json: Json,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<SteadyBeatContract.State, SteadyBeatContract.SideEffect> {
    private val args = savedStateHandle.toRoute<Route.SteadyBeatGame>()
    private val roomId: String = checkNotNull(savedStateHandle["roomId"])
    private val playerId: String = checkNotNull(savedStateHandle["playerId"])
    private val settingJson: String = checkNotNull(savedStateHandle["settingJson"])
    private val deviceAddress: String =
        BuildConfig.BLUETOOTH_MAC_ADDRESS //todo checkNotNull(savedStateHandle["deviceAddress"])

    override val container = container<SteadyBeatContract.State, SteadyBeatContract.SideEffect>(
        initialState = SteadyBeatContract.State.Connecting
    ) {
        connectToGame()
    }

    private var gameTimerJob: Job? = null
    private var heartRateJob: Job? = null

    fun onIntent(intent: SteadyBeatContract.Intent) = when (intent) {
        is SteadyBeatContract.Intent.PauseClicked -> handlePause()
        is SteadyBeatContract.Intent.ResumeClicked -> handleResume()
        is SteadyBeatContract.Intent.QuitClicked -> handleQuit()
        is SteadyBeatContract.Intent.ConfirmQuit -> handleConfirmQuit()
        is SteadyBeatContract.Intent.CancelQuit -> handleCancelQuit()
        is SteadyBeatContract.Intent.RetryConnection -> handleRetryConnection()
    }

    private fun connectToGame() = intent {
        viewModelScope.launch {
            syncHeartbeatUseCase(playerId)
        }

        try {
            gameRepository.observeGameEvents()
                .onEach { handleGameEvent(it) }
                .catch { Log.e("Viewmodel", "Game Event Error", it) }
                .launchIn(viewModelScope)

            val gameSetting = try {
                json.decodeFromString<GameSetting>(settingJson)
            } catch (e: Exception) {
                throw RuntimeException("Settings parsing failed")
            }

            reduce {
                SteadyBeatContract.State.Playing(
                    gameStatus = GameStatus(
                        totalTime = gameSetting.timeLimit,
                        players = gameSetting.players,
                        limit = Limit(gameSetting.bpmMin, gameSetting.bpmMax),
                        currentHeartRate = 0,
                        currentRank = 0,
                        deviationFromTarget = 0
                    ),
                    isPaused = false
                )
            }

            startGameTimer()
            startHeartRateMonitoring()

        } catch (e: Exception) {
            reduce {
                SteadyBeatContract.State.Error(
                    message = e.message ?: "Failed to connect",
                    canRetry = true
                )
            }
        }
    }

    private fun handleGameEvent(event: GameEvent) = intent {
        when (event) {
            is GameEvent.HeartbeatUpdate -> {
                handleHeartbeatUpdate(event)
            }

            is GameEvent.GameEnded -> {
                gameTimerJob?.cancel()
                reduce { SteadyBeatContract.State.Finished }
                val resultJson = json.encodeToString(event.results)

                postSideEffect(
                    SteadyBeatContract.SideEffect.NavigateToResult(
                        roomId = roomId,
                        myPlayerId = playerId,
                        resultJson = resultJson
                    )
                )
            }

            is GameEvent.ErrorDelivered -> {
                reduce {
                    SteadyBeatContract.State.Error(
                        message = event.message,
                        canRetry = true
                    )
                }
            }

            is GameEvent.PlayerLeft -> TODO()
        }
    }

    private fun handleHeartbeatUpdate(event: GameEvent.HeartbeatUpdate) = intent {
        val rank = event.players.first { it.id == playerId }.rank ?: 0

        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent
        val currentStatus = currentState.gameStatus.copy(
            players = event.players,
            currentRank = rank
        )

        reduce {
            currentState.copy(gameStatus = currentStatus)
        }
    }

    private fun startGameTimer() {
        gameTimerJob?.cancel()
        gameTimerJob = viewModelScope.launch {

            while (true) {
                delay(1000)

                intent {
                    val currentState =
                        state as? SteadyBeatContract.State.Playing ?: return@intent
                    val currentStatus = currentState.gameStatus

                    val newElapsedTime = currentStatus.elapsedTime + 1

                    if (newElapsedTime <= currentStatus.totalTime) {
                        reduce {
                            currentState.copy(
                                gameStatus = currentStatus.copy(elapsedTime = newElapsedTime)
                            )
                        }
                    } else {
                        gameTimerJob?.cancel()
                    }
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun startHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = viewModelScope.launch {
            launch {
                try {
                    heartbeatRepository.connect(deviceAddress)
                } catch (e: Exception) {
                    Log.e("SteadyBeatViewModel", "Connection failed", e)
                }
            }

            heartbeatRepository.observeHeartRate()
                .sample(500L)
                .collect { bpm ->
                    Log.d("SteadyBeatViewModel", "Heart Rate Update: $bpm")
                    updateHeartRate(bpm)
                }
        }
    }

    private fun updateHeartRate(newHeartRate: Int) = intent {
        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent
        val currentStatus = currentState.gameStatus
        val newPlayers = currentState.gameStatus.players.map {
            if (it.id == playerId)
                it.copy(bpm = newHeartRate)
            else it
        }
        val deviation = newHeartRate - currentState.targetBpm

        reduce {
            currentState.copy(
                gameStatus = currentStatus.copy(
                    currentHeartRate = newHeartRate,
                    deviationFromTarget = deviation,
                    players = newPlayers
                )
            )
        }
    }

    private fun handlePause() = intent {
        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent

        reduce {
            currentState.copy(isPaused = true)
        }

        gameTimerJob?.cancel()
        heartRateJob?.cancel()
    }

    private fun handleResume() = intent {
        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent

        reduce {
            currentState.copy(isPaused = false)
        }

        startGameTimer()
        startHeartRateMonitoring()
    }

    private fun handleQuit() = intent {
        postSideEffect(
            SteadyBeatContract.SideEffect.ShowQuitDialog("Are you sure you want to quit?")
        )
    }

    private fun handleConfirmQuit() = intent {
        gameTimerJob?.cancel()
        heartRateJob?.cancel()

        postSideEffect(SteadyBeatContract.SideEffect.NavigateBack)
    }

    private fun handleCancelQuit() = intent {
    }

    private fun handleRetryConnection() = intent {
        connectToGame()
    }

    override fun onCleared() {
        super.onCleared()
        gameTimerJob?.cancel()
        heartRateJob?.cancel()
    }
}
