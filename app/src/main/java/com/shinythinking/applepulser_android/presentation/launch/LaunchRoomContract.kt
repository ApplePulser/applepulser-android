package com.shinythinking.applepulser_android.presentation.launch

import com.shinythinking.applepulser_android.domain.model.RoomInfo

class LaunchRoomContract {

    data class State(
        val isLoading: Boolean = true,
        val roomInfo: RoomInfo? = null,
        val error: String? = null
    )

    sealed interface Intent {
        data object OnEnterClicked : Intent
        data object OnCopyCodeClicked : Intent
        data object OnShareClicked : Intent
        data object OnRetryClicked : Intent
    }

    sealed interface SideEffect {
        data class NavigateToHostRoom(val playerId: String, val roomId: String) : SideEffect
        data class CopyToClipboard(val code: String) : SideEffect
        data class ShareRoomInfo(val code: String) : SideEffect
        data class ShowToast(val message: String) : SideEffect
        data object NavigateBack : SideEffect
    }
}