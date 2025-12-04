package com.shinythinking.applepulser_android.presentation.host.hostRoom

import com.shinythinking.applepulser_android.domain.model.RoomInfo

class HostRoomContract {
    data class State(
        val roomId: String,
        val myPlayerId: String,
        val roomInfo: RoomInfo? = null,
        val isReady: Boolean = false,
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    sealed interface Intent {
        data object StartGameClicked : Intent
        data object BackClicked : Intent
        data object LeaveRoom : Intent
        data object ErrorDismissed : Intent
    }

    sealed interface SideEffect {
        data class NavigateToGameSetting(val roomId: String, val playerId: String) : SideEffect
        data object NavigateBack : SideEffect
        data class ShowToast(val message: String) : SideEffect
    }
}