package com.shinythinking.applepulser_android.presentation.client.enterByCode

import androidx.lifecycle.ViewModel
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import com.shinythinking.applepulser_android.domain.repository.UserSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CodeInputViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userSessionRepository: UserSessionRepository
) : ContainerHost<CodeInputContract.State, CodeInputContract.SideEffect>, ViewModel() {

    override val container =
        container<CodeInputContract.State, CodeInputContract.SideEffect>(CodeInputContract.State())

    fun onIntent(intent: CodeInputContract.Intent) = when (intent) {
        is CodeInputContract.Intent.CodeChanged -> onCodeChanged(intent.code)
        is CodeInputContract.Intent.NicknameChanged -> onNicknameChanged(intent.nickname)
        is CodeInputContract.Intent.VerifyCode -> verifyCode()
        is CodeInputContract.Intent.SubmitCode -> joinRoom()
        is CodeInputContract.Intent.DismissDialog -> dismissDialog()
    }

    private fun onCodeChanged(code: String) = intent {
        reduce {
            state.copy(roomCode = code, isCodeError = false)
        }
    }

    private fun onNicknameChanged(nickname: String) = intent {
        reduce { state.copy(nickname = nickname) }
    }

    private fun verifyCode() = intent {
        val code = state.roomCode
        if (code.length != 6) {
            reduce { state.copy(isCodeError = true) }
            return@intent
        }

        reduce { state.copy(isLoading = true, isCodeError = false) }

        try {
            // todo!! 좀 짜치는 구현 API를 요청할 시간이 없다!!!!!!!!!
            // todo! 방은 무조건 존재한다 sosad
            reduce { state.copy(isLoading = false, showNicknameDialog = true) }

        } catch (e: Exception) {
            reduce { state.copy(isLoading = false, isCodeError = true) }
        }
    }

    private fun joinRoom() = intent {
        val nick = state.nickname
        if (nick.isBlank()) {
            postSideEffect(CodeInputContract.SideEffect.ShowToast("Please enter a nickname"))
            return@intent
        }

        reduce { state.copy(isLoading = true) }

        try {
            val roomInfo = roomRepository.joinRoom(
                userName = state.nickname,
                roomCode = state.roomCode
            )

            // todo UserId도 저장해야.. 하지만 아직 API 가 안 뚫림
            userSessionRepository.saveRoomId(roomInfo.roomId)

            reduce {
                state.copy(
                    isLoading = false,
                    showNicknameDialog = false
                )
            }
            postSideEffect(
                CodeInputContract.SideEffect.NavigateToRoom(

                    roomId = roomInfo.roomId,
                    playerId = roomInfo.myPlayerId ?: throw Exception("No player id"),
                )
            )
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false) }
            postSideEffect(CodeInputContract.SideEffect.ShowToast(e.message ?: "Failed to join"))
        }
    }

    private fun dismissDialog() = intent {
        reduce { state.copy(showNicknameDialog = false) }
    }
}