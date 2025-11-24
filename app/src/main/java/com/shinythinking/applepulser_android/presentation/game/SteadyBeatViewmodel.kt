package com.shinythinking.applepulser_android.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.applepulser_android.domain.model.GameStatus
import com.shinythinking.applepulser_android.domain.model.Limit
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SteadyBeatViewmodel @Inject constructor(
    // TODO:  HeartRateSensor, WebSocketManager, GameRepository
) : ViewModel(), ContainerHost<SteadyBeatContract.State, SteadyBeatContract.SideEffect> {

    override val container = container<SteadyBeatContract.State, SteadyBeatContract.SideEffect>(
        initialState = SteadyBeatContract.State.Connecting
    )

    private var gameTimerJob: Job? = null
    private var heartRateJob: Job? = null

    init {
        connectToGame()
    }

    fun onIntent(intent: SteadyBeatContract.Intent) = when (intent) {
        is SteadyBeatContract.Intent.PauseClicked -> handlePause()
        is SteadyBeatContract.Intent.ResumeClicked -> handleResume()
        is SteadyBeatContract.Intent.QuitClicked -> handleQuit()
        is SteadyBeatContract.Intent.ConfirmQuit -> handleConfirmQuit()
        is SteadyBeatContract.Intent.CancelQuit -> handleCancelQuit()
        is SteadyBeatContract.Intent.RankAnimationFinished -> handleRankAnimationFinished()
        is SteadyBeatContract.Intent.RetryConnection -> handleRetryConnection()
    }

    private fun connectToGame() = intent {
        reduce { SteadyBeatContract.State.Connecting }

        try {
            // TODO: WebSocket 연결 및 게임 상태 구독
            delay(1000)

            val mockGameStatus = GameStatus(
                currentHeartRate = 145,
                currentRank = 3,
                targetBpm = 140,
                deviationFromTarget = 5,
                elapsedTime = 0,
                totalTime = 300,
                players = listOf(
                    Player("user1", "신바다", 140, 1, PlayerType.RED),
                    Player("user2", "한예준", 135, 2, PlayerType.WHITE),
                    Player("user3", "홍사인", 145, 3, PlayerType.YELLOW),
                    Player("user4", "김나경", 120, 4, PlayerType.GREEN)
                ),
                limit = Limit(120, 160)
            )

            reduce {
                SteadyBeatContract.State.Playing(gameStatus = mockGameStatus)
            }

            startGameTimer()
            startHeartRateMonitoring()
        } catch (e: Exception) {
            reduce {
                SteadyBeatContract.State.Error(
                    message = e.message ?: "Failed to connect to game",
                    canRetry = true
                )
            }
        }
    }

    private fun startGameTimer() {
        gameTimerJob?.cancel()
        gameTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)

                intent {
                    val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent
                    val currentStatus = currentState.gameStatus
                    val newElapsedTime = currentStatus.elapsedTime + 1

                    if (newElapsedTime >= currentStatus.totalTime) {
                        gameTimerJob?.cancel()
                        postSideEffect(SteadyBeatContract.SideEffect.NavigateToResult)
                        return@intent
                    }

                    reduce {
                        currentState.copy(
                            gameStatus = currentStatus.copy(elapsedTime = newElapsedTime)
                        )
                    }
                }
            }
        }
    }

    private fun startHeartRateMonitoring() {
        heartRateJob?.cancel()
        heartRateJob = viewModelScope.launch {
            // TODO: 실제 센서에서 심박수 데이터 받기

            // 이건 일단 mock
            while (true) {
                delay(1000)
                val mockHeartRate = (120..160).random()
                updateHeartRate(mockHeartRate)
            }
        }
    }

    private fun updateHeartRate(newHeartRate: Int) = intent {
        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent
        val currentStatus = currentState.gameStatus
        val oldRank = currentStatus.currentRank

        // TODO: 서버로 심박수 전송 및 순위 계산

        // 이것도 mock
        val newRank = if (Math.random() > 0.8) {
            (1..currentStatus.players.size).random()
        } else {
            oldRank
        }

        val deviation = newHeartRate - currentStatus.targetBpm

        val updatedPlayers = currentStatus.players.map { player ->
            if (player.rank == oldRank) {
                player.copy(heartRate = newHeartRate, rank = newRank)
            } else {
                player
            }
        }.sortedBy { it.rank }

        reduce {
            currentState.copy(
                gameStatus = currentStatus.copy(
                    currentHeartRate = newHeartRate,
                    currentRank = newRank,
                    deviationFromTarget = deviation,
                    players = updatedPlayers
                )
            )
        }

        if (oldRank != newRank) {
            reduce {
                val updatedState = state as? SteadyBeatContract.State.Playing
                updatedState!!.copy(
                    rankAnimation = SteadyBeatContract.RankChangeAnimation(
                        oldRank = oldRank,
                        newRank = newRank
                    )
                )
            }
            postSideEffect(SteadyBeatContract.SideEffect.VibrateRankChange)
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

    private fun handleRankAnimationFinished() = intent {
        val currentState = state as? SteadyBeatContract.State.Playing ?: return@intent

        reduce {
            currentState.copy(rankAnimation = null)
        }
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
