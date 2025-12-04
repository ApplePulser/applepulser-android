package com.shinythinking.applepulser_android.presentation.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LaunchRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ContainerHost<LaunchRoomContract.State, LaunchRoomContract.SideEffect>, ViewModel() {

    override val container = container<LaunchRoomContract.State, LaunchRoomContract.SideEffect>(
        LaunchRoomContract.State()
    ) {
        createRoom()
    }

    private fun createRoom() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        try {
            val tempNickname = "Host"

            val roomInfo = roomRepository.createRoom(tempNickname)
            Log.d("LaunchRoomViewModel", "Room created: ${roomInfo.roomId}")

            reduce {
                state.copy(
                    isLoading = false,
                    roomInfo = roomInfo
                )
            }
        } catch (e: Exception) {
            reduce {
                state.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create room"
                )
            }
            postSideEffect(LaunchRoomContract.SideEffect.ShowToast("Connection Failed"))
        }
    }

    fun onIntent(intent: LaunchRoomContract.Intent) = when (intent) {
        is LaunchRoomContract.Intent.OnEnterClicked -> handleEnter()
        is LaunchRoomContract.Intent.OnCopyCodeClicked -> handleCopy()
        is LaunchRoomContract.Intent.OnShareClicked -> handleShare()
        is LaunchRoomContract.Intent.OnRetryClicked -> createRoom()
    }

    private fun handleEnter() = intent {
        val info = state.roomInfo ?: return@intent
        if (info.myPlayerId == null) {
            state.copy(isLoading = true, error = "Player ID not found")
            Log.e("LaunchRoomViewModel", "Player ID not found")
        }
        val hostId = info.myPlayerId ?: return@intent

        postSideEffect(
            LaunchRoomContract.SideEffect.NavigateToHostRoom(
                playerId = hostId,
                roomId = info.roomId
            )
        )
    }

    private fun handleCopy() = intent {
        state.roomInfo?.roomCode?.let { code ->
            postSideEffect(LaunchRoomContract.SideEffect.CopyToClipboard(code))
            postSideEffect(LaunchRoomContract.SideEffect.ShowToast("Code copied!"))
        }
    }

    private fun handleShare() = intent {
        state.roomInfo?.roomCode?.let { code ->
            postSideEffect(LaunchRoomContract.SideEffect.ShareRoomInfo(code))
        }
    }
}