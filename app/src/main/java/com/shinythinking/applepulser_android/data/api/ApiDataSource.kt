package com.shinythinking.applepulser_android.data.api

import com.shinythinking.applepulser_android.BuildConfig
import com.shinythinking.applepulser_android.data.dto.CreateRoomRequest
import com.shinythinking.applepulser_android.data.dto.CreateRoomResponse
import com.shinythinking.applepulser_android.data.dto.GetRoomInfoResponse
import com.shinythinking.applepulser_android.data.dto.JoinRoomRequest
import com.shinythinking.applepulser_android.data.dto.JoinRoomResponse
import com.shinythinking.applepulser_android.data.dto.LeaveRoomRequest
import com.shinythinking.applepulser_android.data.dto.LeaveRoomResponse
import com.shinythinking.applepulser_android.data.dto.StartGameRequest
import com.shinythinking.applepulser_android.data.dto.StartGameResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL

        private const val ROOM_CREATE = "$BASE_URL/rooms/"
        private const val ROOM_INFO = "$BASE_URL/rooms/{roomId}/"
        private const val ROOM_JOIN = "$BASE_URL/rooms/join/"
        private const val ROOM_LEAVE = "$BASE_URL/rooms/{roomId}/leave/"
        private const val ROOM_DELETE = "$BASE_URL/rooms/{roomId}/?player_id={playerId}"
        private const val GAME_START = "$BASE_URL/rooms/{roomId}/start/"
    }

    suspend fun createRoom(request: CreateRoomRequest): CreateRoomResponse {
        return client.post(ROOM_CREATE) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getRoomInfo(roomId: String): GetRoomInfoResponse {
        return client.get(ROOM_INFO.replace("{roomId}", roomId)).body()
    }

    suspend fun joinRoom(request: JoinRoomRequest): JoinRoomResponse {
        return client.post(ROOM_JOIN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun leaveRoom(request: LeaveRoomRequest, roomId: String): LeaveRoomResponse {
        return client.post(ROOM_LEAVE.replace("{roomId}", roomId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteRoom(roomId: String, playerId: String) {
        client.delete(
            ROOM_DELETE
                .replace("{roomId}", roomId)
                .replace("{playerId}", playerId)
        )
    }

    suspend fun startGame(request: StartGameRequest, roomId: String): StartGameResponse {
        return client.post(GAME_START.replace("{roomId}", roomId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}