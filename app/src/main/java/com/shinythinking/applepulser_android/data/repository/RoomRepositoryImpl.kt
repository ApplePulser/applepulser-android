package com.shinythinking.applepulser_android.data.repository

import android.util.Log
import com.shinythinking.applepulser_android.data.api.ApiDataSource
import com.shinythinking.applepulser_android.data.dto.CreateRoomRequest
import com.shinythinking.applepulser_android.data.dto.ErrorMessage
import com.shinythinking.applepulser_android.data.dto.GameStartMessage
import com.shinythinking.applepulser_android.data.dto.JoinRoomRequest
import com.shinythinking.applepulser_android.data.dto.LeaveRoomRequest
import com.shinythinking.applepulser_android.data.dto.PlayerJoinedMessage
import com.shinythinking.applepulser_android.data.dto.PlayerLeftMessage
import com.shinythinking.applepulser_android.data.dto.PlayerReadyMessage
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
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
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

    override suspend fun joinRoom(userName: String, roomCode: String): RoomInfo {
        if (!isValidRoomCode(roomCode)) {
            throw InvalidRoomCodeException()
        }

        try {
            val request = JoinRoomRequest(
                playerName = userName,
                roomCode = roomCode
            )

            val response = apiDataSource.joinRoom(request)

            socketDataSource.connect(response.roomId)

            return response.toDomain()
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

    override suspend fun startGame(
        playerId: String,
        roomId: String,
        gameSetting: GameSetting
    ): GameSetting {
        try {
            val request = StartGameRequest(
                playerId = playerId,
                mode = gameSetting.gameMode.title,
                timeLimitSeconds = gameSetting.timeLimit,
                bpmMax = gameSetting.bpmMax,
                bpmMin = gameSetting.bpmMin,
            )
            val response = apiDataSource.startGame(request, roomId)

            return response.toDomain()

        } catch (e: ClientRequestException) {
            throw GeneralRoomException("Client Error: ${e.response.status}")
        } catch (e: ServerResponseException) {
            throw GeneralRoomException("Server Error (500): Please check server logs.")
        } catch (e: Exception) {
            throw GeneralRoomException("Failed to start game: ${e.message}")
        }
    }


    override suspend fun setReadyStatus(playerId: String, isReady: Boolean) {
        val status = if (isReady) "true" else "true"

        val message = PlayerReadyMessage(
            playerId = playerId,
            isReady = status
//            status = status
        )

        socketDataSource.sendMessage(message)
    }

    override fun observeRoomEvents(): Flow<RoomEvent> {
        return socketDataSource.incomingMessages
            .mapNotNull { message ->
                Log.d("RoomEvent", "Received message: $message")
                when (message) {
                    is GameStartMessage -> message.toDomainEvent()
                    is PlayerJoinedMessage -> message.toDomainEvent()
                    is PlayerLeftMessage -> message.toDomainEvent()
                    is PlayerReadyMessage -> message.toDomainEvent()
                    is ErrorMessage -> message.toDomainEvent()
                    else -> null
                }
            }
    }

    private fun isValidRoomCode(code: String): Boolean {
        return code.length == 6 && code.all { it.isDigit() }
    }
}
