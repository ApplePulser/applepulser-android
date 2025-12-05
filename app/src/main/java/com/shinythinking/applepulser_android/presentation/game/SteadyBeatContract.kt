package com.shinythinking.applepulser_android.presentation.game

import com.shinythinking.applepulser_android.domain.model.GameStatus
import com.shinythinking.applepulser_android.domain.model.Player

object SteadyBeatContract {

    sealed interface State {
        data object Connecting : State

        data class Playing(
            val gameStatus: GameStatus,
            val isPaused: Boolean = false,
        ) : State {
            val currentHeartRate: Int
                get() = gameStatus.currentHeartRate ?: 0

            val currentRank: Int
                get() = gameStatus.currentRank ?: 0

            val deviationFromTarget: Int
                get() = gameStatus.deviationFromTarget ?: 0

            val players: List<Player>
                get() = gameStatus.players

            val elapsedTimeFormatted: String
                get() = gameStatus.elapsedTimeFormatted

            val totalTimeFormatted: String
                get() = gameStatus.totalTimeFormatted

            val targetBpm: Int
                get() = (gameStatus.limit.max + gameStatus.limit.min) / 2

        }

        data object Finished : State

        data class Error(
            val message: String,
            val canRetry: Boolean = true
        ) : State
    }

    sealed interface Intent {
        object PauseClicked : Intent
        object ResumeClicked : Intent
        object QuitClicked : Intent
        object ConfirmQuit : Intent
        object CancelQuit : Intent
        object RetryConnection : Intent
    }

    sealed interface SideEffect {
        data class NavigateToResult(
            val roomId: String,
            val myPlayerId: String,
            val resultJson: String
        ) : SideEffect

        object NavigateBack : SideEffect
        data class ShowToast(val message: String) : SideEffect
        data class ShowQuitDialog(val message: String) : SideEffect
    }
}