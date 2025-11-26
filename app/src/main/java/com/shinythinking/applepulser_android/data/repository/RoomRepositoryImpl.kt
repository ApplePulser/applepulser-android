package com.shinythinking.applepulser_android.data.repository

import com.shinythinking.applepulser_android.data.api.ApiDataSource
import com.shinythinking.applepulser_android.data.dto.CreateRoomRequest
import com.shinythinking.applepulser_android.data.dto.ErrorMessage
import com.shinythinking.applepulser_android.data.dto.GameStartedMessage
import com.shinythinking.applepulser_android.data.dto.JoinRoomRequest
import com.shinythinking.applepulser_android.data.dto.LeaveRoomRequest
import com.shinythinking.applepulser_android.data.dto.PlayerJoinedMessage
import com.shinythinking.applepulser_android.data.dto.PlayerLeftMessage
import com.shinythinking.applepulser_android.data.dto.SendReadyStatusMessage
import com.shinythinking.applepulser_android.data.dto.StartGameRequest
import com.shinythinking.applepulser_android.data.dto.toDomain
import com.shinythinking.applepulser_android.data.dto.toDomainEvent
import com.shinythinking.applepulser_android.data.network.SocketDataSource
import com.shinythinking.applepulser_android.domain.exception.GeneralRoomException
import com.shinythinking.applepulser_android.domain.exception.InvalidRoomCodeException
import com.shinythinking.applepulser_android.domain.exception.RoomException
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl @Inject constructor(
    private val apiDataSource: ApiDataSource,
    private val socketDataSource: SocketDataSource
) : RoomRepository {
    override suspend fun createRoom(userName: String): RoomInfo {
        try {
            val request = CreateRoomRequest(userName = userName)
            val response = apiDataSource.createRoom(request)

            socketDataSource.connect(response.roomId)

            return response.toDomain()
        } catch (e: RoomException) {
            throw e
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to create room: ${e.message}")
        }
    }

    override suspend fun getRoomInfo(roomId: String): RoomInfo {
        try {
            val response = apiDataSource.getRoomInfo(roomId)

            val roomInfo = response.toDomain()

            return roomInfo
        } catch (e: RoomException) {
            throw e
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to get room info: ${e.message}")
        }
    }

    override suspend fun joinRoom(userId: String, roomCode: String): RoomInfo {
        if (!isValidRoomCode(roomCode)) {
            throw InvalidRoomCodeException()
        }

        try {
            val request = JoinRoomRequest(
                playerId = userId
            )

            // todo code 주고 info 받는 거 만들어야 함.

            val response = apiDataSource.joinRoom(request, roomCode)

            socketDataSource.connect(roomCode)

            getRoomInfo(response.room.roomId)
            val roomInfo = response.toDomain()

            return roomInfo
        } catch (e: RoomException) {
            throw e
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to join room: ${e.message}")
        }
    }

    override suspend fun leaveRoom(playerId: String, roomId: String): Boolean {
        val request = LeaveRoomRequest(playerId)

        try {
            val response = apiDataSource.leaveRoom(request, roomId)

            socketDataSource.disconnect()
            return response.message == "Successfully left the room"
        } catch (e: RoomException) {
            throw e
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to leave room: ${e.message}")
        }
    }

    override suspend fun deleteRoom(roomId: String, playerId: String): Boolean {
        try {
            apiDataSource.deleteRoom(roomId, playerId)
            socketDataSource.disconnect()
            return true
        } catch (e: RoomException) {
            throw e
        }
    }

    override suspend fun startGame(playerId: String, gameSetting: GameSetting): Boolean {
        if (!gameSetting.canStart) {
            throw GeneralRoomException("Cannot start game: conditions not met")
        }

        try {
            val request = StartGameRequest(
                playerId = playerId,
                mode = gameSetting.gameMode.title,
                timeLimit = gameSetting.timeLimit,
                bpmMax = gameSetting.bpmMax,
                bpmMin = gameSetting.bpmMin,
            )
            val response = apiDataSource.startGame(request, gameSetting.roomId)

            return response.message == "Game started"

        } catch (e: RoomException) {
            throw e
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to start game: ${e.message}")
        }
    }


    override suspend fun setReadyStatus(playerId: String, isReady: Boolean) {
        val status = if (isReady) "ready" else "not_ready"

        val message = SendReadyStatusMessage(
            playerId = playerId,
            status = status
        )

        socketDataSource.sendMessage(message)
    }

    override fun observeRoomEvents(): Flow<RoomEvent> {
        return socketDataSource.incomingMessages
            .mapNotNull { message ->
                when (message) {
                    is PlayerJoinedMessage -> message.toDomainEvent()
                    is PlayerLeftMessage -> message.toDomainEvent()
                    is GameStartedMessage -> message.toDomainEvent()
                    is ErrorMessage -> RoomEvent.Error(message.message)
                    else -> null
                }
            }
    }

    private fun isValidRoomCode(code: String): Boolean {
        return code.length == 6 && code.all { it.isDigit() }
    }
}
