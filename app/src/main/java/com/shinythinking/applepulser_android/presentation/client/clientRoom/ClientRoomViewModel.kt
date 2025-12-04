package com.shinythinking.applepulser_android.presentation.client.clientRoom

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.RoomStatus
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import com.shinythinking.applepulser_android.presentation.client.clientRoom.ClientRoomContract.SideEffect.NavigateToGame
import com.shinythinking.applepulser_android.presentation.client.clientRoom.ClientRoomContract.SideEffect.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ClientRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roomRepository: RoomRepository,
    private val gameRepository: GameRepository,
    private val json: Json
) : ViewModel(), ContainerHost<ClientRoomContract.State, ClientRoomContract.SideEffect> {

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])
    private val playerId: String = checkNotNull(savedStateHandle["playerId"])

    override val container: Container<ClientRoomContract.State, ClientRoomContract.SideEffect> =
        container(
            ClientRoomContract.State(
                isLoading = true,
                roomId = roomId,
                myPlayerId = playerId,
                roomInfo = RoomInfo(
                    roomId = roomId,
                    players = listOf(),
                    roomCode = "",
                    status = RoomStatus.WAITING,
                    myPlayerId = playerId,
                )
            )
        ) {
            loadRoomInfo()
            subscribeToRoomEvents()
        }

    fun onIntent(intent: ClientRoomContract.Intent) = when (intent) {
        is ClientRoomContract.Intent.ReadyClicked -> handleReadyClicked()
        is ClientRoomContract.Intent.BackClicked -> handleBackClicked()
        is ClientRoomContract.Intent.LeaveRoom -> handleLeaveRoom()
        is ClientRoomContract.Intent.ErrorDismissed -> handleErrorDismissed()
    }

    private fun subscribeToRoomEvents() {
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
            postSideEffect(ShowToast("Failed to load room info"))
            // todo 다시 시도
            postSideEffect(ClientRoomContract.SideEffect.NavigateBack)
        }
    }


    private fun handleRoomEvent(event: RoomEvent) {
        when (event) {
            is RoomEvent.PlayerReady -> intent {
                val currentPlayer = state.roomInfo.players.map {
                    if (it.id == event.playerId)
                        it.copy(status = if (event.isReady) PlayerStatus.READY else PlayerStatus.WAITING)
                    else
                        it
                }

                reduce {
                    state.copy(roomInfo = state.roomInfo.copy(players = currentPlayer))
                }
            }

            is RoomEvent.PlayerJoined -> intent {
                val newPlayers = (state.roomInfo.players.plus(event.player))

                reduce {
                    state.copy(roomInfo = state.roomInfo.copy(players = newPlayers))
                }

                loadRoomInfo()
                Log.d(
                    "ClientRoomViewModel",
                    "PlayerJoined: ${state.roomInfo.players.joinToString(" ") { it.name }}"
                )
            }

            is RoomEvent.PlayerLeft -> intent {
                val newPlayers =
                    state.roomInfo.players.filter { it.id != event.playerId }
                state.copy(roomInfo = state.roomInfo.copy(players = newPlayers))

                loadRoomInfo()
            }

            is RoomEvent.GameStarted -> intent {
                reduce {
                    state.copy(isLoading = true)
                }
                postSideEffect(
                    NavigateToGame(
                        roomId = state.roomId,
                        playerId = state.myPlayerId,
                        gameSetting = json.encodeToString<GameSetting>(event.gameSetting)
                    )
                )
            }

            is RoomEvent.ErrorDelivered -> intent {
                postSideEffect(ShowToast(event.message))
            }


        }
    }

    private fun handleReadyClicked() = intent {
        try {
            roomRepository.setReadyStatus(
                playerId = playerId,
                isReady = !state.isReady
            )

            reduce {
                state.copy(isReady = !state.isReady)
            }
        } catch (e: Exception) {
            reduce {
                state.copy(errorMessage = e.message ?: "Failed to update ready status")
            }
        }
    }

    private fun handleBackClicked() = intent {
        try {
            roomRepository.leaveRoom(playerId, roomId)
            postSideEffect(ClientRoomContract.SideEffect.NavigateBack)
        } catch (e: Exception) {
            reduce {
                state.copy(errorMessage = e.message ?: "Failed to leave room")
            }

        }
    }

    private fun handleLeaveRoom() = intent {
        try {
            roomRepository.leaveRoom(playerId, roomId)
            postSideEffect(ClientRoomContract.SideEffect.NavigateBack)
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
