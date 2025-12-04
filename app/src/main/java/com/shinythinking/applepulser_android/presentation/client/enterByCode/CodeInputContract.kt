package com.shinythinking.applepulser_android.presentation.client.enterByCode

object CodeInputContract {
    data class State(
        val roomCode: String = "",
        val nickname: String = "",
        val isCodeError: Boolean = false,
        val showNicknameDialog: Boolean = false,
        val isLoading: Boolean = false
    )

    sealed interface Intent {
        data class CodeChanged(val code: String) : Intent
        data class NicknameChanged(val nickname: String) : Intent
        data object VerifyCode : Intent
        data object SubmitCode : Intent
        data object DismissDialog : Intent
    }

    sealed interface SideEffect {
        data class NavigateToRoom(val roomId: String) : SideEffect
        data class ShowToast(val message: String) : SideEffect
    }
}