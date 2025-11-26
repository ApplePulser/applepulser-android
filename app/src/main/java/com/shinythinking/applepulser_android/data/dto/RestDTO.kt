package com.shinythinking.applepulser_android.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("host_nickname") val userName: String,
)

@Serializable
data class CreateRoomResponse(
    @SerialName("room_id") val roomId: String,
    @SerialName("room_code") val roomCode: String,
    @SerialName("qr_code_url") val qrCode: String,
    @SerialName("host") val host: RoomHostDto,
    @SerialName("status") val status: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class RoomHostDto(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String
)

@Serializable
data class GetRoomInfoResponse(
    @SerialName("room_id") val roomId: String,
    @SerialName("room_code") val roomCode: String,
    @SerialName("status") val status: String,
    @SerialName("max_players") val maxPlayers: Int,
    @SerialName("players") val players: List<RoomPlayerDto>,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
data class RoomPlayerDto(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("status") val status: String,
    @SerialName("is_host") val isHost: Boolean
)

@Serializable
data class JoinRoomRequest(
    @SerialName("player_id") val playerId: String,
)

@Serializable
data class JoinRoomResponse(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("room") val room: RoomSimpleInfoDto,
)

@Serializable
data class RoomSimpleInfoDto(
    @SerialName("room_id") val roomId: String,
    @SerialName("room_code") val roomCode: String,
    @SerialName("mode") val mode: String? = null,
    @SerialName("status") val status: String
)

@Serializable
data class LeaveRoomRequest(
    @SerialName("player_id") val playerId: String,
)

@Serializable
data class LeaveRoomResponse(
    @SerialName("message") val message: String,
)

@Serializable
data class StartGameRequest(
    @SerialName("player_id") val playerId: String,
    @SerialName("mode") val mode: String,
    @SerialName("time_limit") val timeLimit: Int,
    @SerialName("bpm_min") val bpmMin: Int,
    @SerialName("bpm_max") val bpmMax: Int
)

@Serializable
data class StartGameResponse(
    @SerialName("message") val message: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("status") val status: String,
    @SerialName("game_settings") val gameSettings: GameSettingDto,
    @SerialName("started_at") val startedAt: String
)

@Serializable
data class GameSettingDto(
    @SerialName("mode") val mode: String,
    @SerialName("time_limit") val timeLimit: Int,
    @SerialName("bpm_min") val bpmMin: Int,
    @SerialName("bpm_max") val bpmMax: Int
)
