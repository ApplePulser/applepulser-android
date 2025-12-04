package com.shinythinking.applepulser_android.presentation.client.clientRoom

import com.shinythinking.applepulser_android.domain.model.RoomInfo

class ClientRoomContract {
    data class State(
        val roomId: String,
        val myPlayerId: String,
        val roomInfo: RoomInfo,
        val isReady: Boolean = false,
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    sealed interface Intent {
        data object ReadyClicked : Intent
        data object BackClicked : Intent
        data object LeaveRoom : Intent
        data object ErrorDismissed : Intent

    }

    sealed interface SideEffect {
        data class NavigateToGame(
            val roomId: String,
            val playerId: String,
            val gameSetting: String
        ) : SideEffect

        data object NavigateBack : SideEffect
        data class ShowToast(val message: String) : SideEffect
    }
}