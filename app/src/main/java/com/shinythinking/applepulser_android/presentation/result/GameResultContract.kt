package com.shinythinking.applepulser_android.presentation.result

import com.shinythinking.applepulser_android.domain.model.PlayerResult

class GameResultContract {

    data class State(
        val rankings: List<PlayerResult> = emptyList(),
        val myPlayerId: String = ""
    )

    sealed interface Intent {
        data object OnRecordClicked : Intent
        data object OnFinishClicked : Intent
    }

    sealed interface SideEffect {
        data class NavigateToRecord(
            val playerId: String,
            val roomId: String,
            val resultJson: String
        ) : SideEffect

        data object NavigateToHome : SideEffect
    }
}