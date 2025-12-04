package com.shinythinking.applepulser_android.presentation.record

import com.shinythinking.applepulser_android.domain.model.PlayerResult

class RecordContract {
    data class State(
        val results: List<PlayerResult> = emptyList()
    )

    sealed interface Intent {
        data object OnHomeClicked : Intent
    }

    sealed interface SideEffect {
        data object NavigateToHome : SideEffect
    }
}