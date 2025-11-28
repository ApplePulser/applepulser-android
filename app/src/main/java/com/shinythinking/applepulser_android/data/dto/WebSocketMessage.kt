@file:OptIn(ExperimentalSerializationApi::class)

package com.shinythinking.applepulser_android.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("type")
sealed interface WebSocketMessage

@Serializable
@SerialName("heartbeat")
data class SendHeartbeatMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("bpm") val bpm: Int,
    @SerialName("timestamp") val timestamp: String
) : WebSocketMessage

@Serializable
@SerialName("ready_status")
data class SendReadyStatusMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("status") val status: String
) : WebSocketMessage

@Serializable
@SerialName("heartbeat_broadcast")
data class BroadcastHeartbeatMessage(
    @SerialName("players") val players: List<GamePlayerDto>,
    @SerialName("timestamp") val timestamp: String
) : WebSocketMessage

@Serializable
data class GamePlayerDto(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("bpm") val bpm: Int,
    @SerialName("rank") val rank: Int,
    @SerialName("deviation") val deviation: Int,
)

@Serializable
@SerialName("player_joined")
data class PlayerJoinedMessage(
    @SerialName("player") val player: RoomPlayerDto,
    @SerialName("total_players") val totalPlayers: Int
) : WebSocketMessage

@Serializable
@SerialName("player_left")
data class PlayerLeftMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("total_players") val totalPlayers: Int
) : WebSocketMessage

@Serializable
@SerialName("game_started")
data class GameStartedMessage(
    @SerialName("room_id") val roomId: String,
    @SerialName("mode") val mode: String,
    @SerialName("time_limit") val timeLimit: Int,
    @SerialName("bpm_min") val bpmMin: Int,
    @SerialName("bpm_max") val bpmMax: Int,
    @SerialName("started_at") val startedAt: String
) : WebSocketMessage

@Serializable
@SerialName("game_ended")
data class GameEndedMessage(
    @SerialName("room_id") val roomId: String,
    @SerialName("ended_at") val endedAt: String,
    @SerialName("reason") val reason: String
) : WebSocketMessage

@Serializable
@SerialName("game_result")
data class GameResultMessage(
    @SerialName("room_id") val roomId: String,
    @SerialName("mode") val mode: String,
    @SerialName("rankings") val rankings: List<RankingDto>,
    @SerialName("finished_at") val finishedAt: String
) : WebSocketMessage

@Serializable
data class RankingDto(
    @SerialName("rank") val rank: Int,
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("score") val score: Int,
    @SerialName("stats") val stats: StatsDto,
)

@Serializable
data class StatsDto(
    @SerialName("workout_time") val workoutTime: Int,
    @SerialName("avg_bpm") val avgBpm: Int,
    @SerialName("min_bpm") val minBpm: Int,
    @SerialName("max_bpm") val maxBpm: Int,
    @SerialName("time_in_zone") val timeInZone: Int,
)

@Serializable
@SerialName("error")
data class ErrorMessage(
    @SerialName("error_code") val errorCode: String,
    @SerialName("message") val message: String
) : WebSocketMessage