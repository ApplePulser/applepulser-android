package com.shinythinking.applepulser_android.presentation.home

object HomeContract {
    data class State(
        val isLoading: Boolean = false,
    )

    sealed interface Intent {
        object LaunchRoomClick : Intent
        object EnterRoomClick : Intent
    }

    sealed interface SideEffect {
        object NavigateToLaunchRoom : SideEffect
        object NavigateToEnterRoom : SideEffect
    }
}