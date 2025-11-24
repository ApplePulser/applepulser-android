package com.shinythinking.applepulser_android.presentation.game

import com.shinythinking.applepulser_android.domain.model.GameStatus
import com.shinythinking.applepulser_android.domain.model.Player

object SteadyBeatContract {

    sealed interface State {
        object Connecting : State

        data class Playing(
            val gameStatus: GameStatus,
            val isPaused: Boolean = false,
            val rankAnimation: RankChangeAnimation? = null
        ) : State {
            val currentHeartRate: Int
                get() = gameStatus.currentHeartRate

            val currentRank: Int
                get() = gameStatus.currentRank

            val deviationFromTarget: Int
                get() = gameStatus.deviationFromTarget

            val players: List<Player>
                get() = gameStatus.players

            val elapsedTimeFormatted: String
                get() = gameStatus.elapsedTimeFormatted

            val totalTimeFormatted: String
                get() = gameStatus.totalTimeFormatted

        }

        data class Error(
            val message: String,
            val canRetry: Boolean = true
        ) : State
    }

    data class RankChangeAnimation(
        val oldRank: Int,
        val newRank: Int
    ) {
        val isRankUp: Boolean
            get() = newRank < oldRank

        val message: String
            get() = if (isRankUp) "Rank Up!" else "Rank Down"
    }

    sealed interface Intent {
        object PauseClicked : Intent
        object ResumeClicked : Intent
        object QuitClicked : Intent
        object ConfirmQuit : Intent
        object CancelQuit : Intent
        object RankAnimationFinished : Intent
        object RetryConnection : Intent
    }

    sealed interface SideEffect {
        object NavigateToResult : SideEffect
        object NavigateBack : SideEffect
        object VibrateRankChange : SideEffect
        data class ShowToast(val message: String) : SideEffect
        data class ShowQuitDialog(val message: String) : SideEffect
    }
}