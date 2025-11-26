package com.shinythinking.applepulser_android.domain.repository

import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(userName: String): RoomInfo
    suspend fun getRoomInfo(roomId: String): RoomInfo
    suspend fun joinRoom(userId: String, roomCode: String): RoomInfo
    suspend fun leaveRoom(playerId: String, roomId: String): Boolean
    suspend fun deleteRoom(roomId: String, playerId: String): Boolean
    suspend fun startGame(playerId: String, gameSetting: GameSetting): Boolean
    suspend fun setReadyStatus(playerId: String, isReady: Boolean)
    fun observeRoomEvents(): Flow<RoomEvent>
}
