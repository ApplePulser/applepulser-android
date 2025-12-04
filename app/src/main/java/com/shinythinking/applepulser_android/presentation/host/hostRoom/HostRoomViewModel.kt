package com.shinythinking.applepulser_android.presentation.host.hostRoom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HostRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roomRepository: RoomRepository,
    private val gameRepository: GameRepository
) : ViewModel(), ContainerHost<HostRoomContract.State, HostRoomContract.SideEffect> {

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])
    private val playerId: String = checkNotNull(savedStateHandle["playerId"])

    override val container: Container<HostRoomContract.State, HostRoomContract.SideEffect> =
        container(
            HostRoomContract.State(
                roomId = roomId,
                myPlayerId = playerId,
            )
        ) {
            loadRoomInfo()
            subscribeToRoomEvents()
        }

    fun onIntent(intent: HostRoomContract.Intent) = when (intent) {
        is HostRoomContract.Intent.StartGameClicked -> handleStartClicked()
        is HostRoomContract.Intent.BackClicked -> handleBackClicked()
        is HostRoomContract.Intent.LeaveRoom -> handleLeaveRoom()
        is HostRoomContract.Intent.ErrorDismissed -> handleErrorDismissed()
    }

    private fun subscribeToRoomEvents() = intent {
        roomRepository.observeRoomEvents()
            .onEach { event ->
                handleRoomEvent(event)
            }
            .launchIn(viewModelScope)
    }

    private fun loadRoomInfo() = intent {
        try {
            val response = roomRepository.getRoomInfo(roomId)
            reduce {
                state.copy(
                    isLoading = false,
                    roomInfo = response
                )
            }
        } catch (e: Exception) {
            reduce { state.copy(isLoading = true) }
            postSideEffect(HostRoomContract.SideEffect.ShowToast("Failed to load room info"))
            postSideEffect(HostRoomContract.SideEffect.NavigateBack)
        }
    }


    private fun handleRoomEvent(event: RoomEvent) {
        when (event) {
            is RoomEvent.PlayerJoined -> intent {
                val newPlayers =
                    (state.roomInfo?.players?.plus(event.player)) ?: listOf(event.player)

                reduce {
                    state.copy(roomInfo = state.roomInfo?.copy(players = newPlayers))
                }

                loadRoomInfo()
            }

            is RoomEvent.PlayerLeft -> intent {
                val newPlayers =
                    state.roomInfo?.players?.filter { it.id != event.playerId } ?: emptyList()
                state.copy(roomInfo = state.roomInfo?.copy(players = newPlayers))

                loadRoomInfo()
            }

            is RoomEvent.GameStarted -> intent {
                roomRepository.setReadyStatus(
                    playerId = playerId,
                    isReady = true
                )

                postSideEffect(
                    HostRoomContract.SideEffect.NavigateToGameSetting(
                        roomId = state.roomId,
                        playerId = state.myPlayerId
                    )
                )
            }

            is RoomEvent.ErrorDelivered -> intent {
                postSideEffect(HostRoomContract.SideEffect.ShowToast(event.message))
            }

            is RoomEvent.PlayerReady -> intent {
                val currentPlayer = state.roomInfo?.players?.map {
                    if (it.id == event.playerId) {
                        it.copy(
                            status = if (event.isReady) PlayerStatus.READY else PlayerStatus.WAITING
                        )
                    } else {
                        it
                    }
                }

                reduce {
                    state.copy(
                        roomInfo = state.roomInfo?.copy(players = currentPlayer ?: emptyList())
                    )
                }
            }
        }
    }

    private fun handleStartClicked() = intent {
        try {
            roomRepository.setReadyStatus(
                playerId = playerId,
                isReady = true
            )

            postSideEffect(HostRoomContract.SideEffect.NavigateToGameSetting(roomId, playerId))
        } catch (e: Exception) {
            reduce {
                state.copy(errorMessage = e.message ?: "Failed to update ready status")
            }
        }
    }

    private fun handleBackClicked() = intent {
        try {
            roomRepository.deleteRoom(roomId, playerId)
            postSideEffect(HostRoomContract.SideEffect.NavigateBack)
        } catch (e: Exception) {
            reduce {
                state.copy(errorMessage = e.message ?: "Failed to leave room")
            }

        }
    }

    private fun handleLeaveRoom() = intent {
        try {
            roomRepository.deleteRoom(roomId, playerId)
            postSideEffect(HostRoomContract.SideEffect.NavigateBack)
        } catch (e: Exception) {
            reduce {
                state.copy(errorMessage = e.message ?: "Failed to leave room")
            }
        }
    }

    private fun handleErrorDismissed() = intent {
        reduce {
            state.copy(errorMessage = null)
        }
    }
}
