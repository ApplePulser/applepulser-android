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
@SerialName("player_ready")
data class PlayerReadyMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("is_ready") val isReady: String
//    @SerialName("status") val status: String
) : WebSocketMessage

// 얘는 보내기만
@Serializable
@SerialName("heartbeat")
data class HeartbeatMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("bpm") val bpm: Int,
//    @SerialName("timestamp") val timestamp: String
) : WebSocketMessage

@Serializable
@SerialName("game_start")
data class GameStartMessage(
    @SerialName("total_time") val totalTime: Int,
    @SerialName("min_bpm") val minBpm: Int,
    @SerialName("max_bpm") val maxBpm: Int,
    @SerialName("target_bpm") val targetBpm: Int,
    @SerialName("players") val players: List<GamePlayerDto>
) : WebSocketMessage

@Serializable
data class GamePlayerDto(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("is_host") val isHost: String? = null,
    @SerialName("bpm") val bpm: Int? = null,
    @SerialName("diff") val diff: Int? = null,
    val rank: Int? = null
)

@Serializable
@SerialName("bpm_update")
data class BpmUpdateMessage(
    @SerialName("rankings") val playersRanking: List<GamePlayerDto>
) : WebSocketMessage

@Serializable
@SerialName("player_joined")
data class PlayerJoinedMessage(
    @SerialName("player") val player: RoomPlayerSocketDto,
    @SerialName("total_players") val totalPlayers: Int
) : WebSocketMessage

@Serializable
data class RoomPlayerSocketDto(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("is_host") val isHost: String? = null,
    @SerialName("is_ready") val isReady: String? = null
)

@Serializable
@SerialName("game_end")
data class GameEndMessage(
    @SerialName("results") val results: List<ResultDto>
) : WebSocketMessage

@Serializable
data class ResultDto(
    @SerialName("rank") val rank: Int,
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("min_bpm") val minBpm: Int,
    @SerialName("max_bpm") val maxBpm: Int,
    @SerialName("avg_mae") val avgMae: Double
)

@Serializable
@SerialName("player_disconnected")
data class PlayerLeftMessage(
    @SerialName("player_id") val playerId: String,
    @SerialName("nickname") val nickname: String,
) : WebSocketMessage

@Serializable
@SerialName("error")
data class ErrorMessage(
    @SerialName("error_code") val errorCode: String,
    @SerialName("message") val message: String
) : WebSocketMessage

//@Serializable
//data class GamePlayerBpmDto(
//    @SerialName("player_id") val playerId: String,
//    @SerialName("nickname") val nickname: String,
//    @SerialName("bpm") val bpm: Int,
//    @SerialName("diff") val diff: Int
//)

//@Serializable
//@SerialName("game_started")
//data class GameStartedMessage(
//    @SerialName("room_id") val roomId: String,
//    @SerialName("mode") val mode: String,
//    @SerialName("time_limit_seconds") val timeLimit: Int,
//    @SerialName("bpm_min") val bpmMin: Int,
//    @SerialName("bpm_max") val bpmMax: Int,
//    @SerialName("started_at") val startedAt: String
//) : WebSocketMessage
//@Serializable
//@SerialName("heart_rate")
//data class BroadcastHeartbeatMessage(
////    @SerialName("players") val players: List<GamePlayerDto>,
//    @SerialName("player_id") val playerId: String,
//    @SerialName("bpm") val bpm: Int,
////    @SerialName("timestamp") val timestamp: String
//) : WebSocketMessage

//@Serializable
//data class GamePlayerDto(
//    @SerialName("player_id") val playerId: String,
//    @SerialName("nickname") val nickname: String,
//    @SerialName("bpm") val bpm: Int,
//    @SerialName("rank") val rank: Int,
//    @SerialName("deviation") val deviation: Int,
//)


//@Serializable
//@SerialName("game_result")
//data class GameResultMessage(
//    @SerialName("room_id") val roomId: String,
//    @SerialName("mode") val mode: String,
//    @SerialName("rankings") val rankings: List<RankingDto>,
//    @SerialName("finished_at") val finishedAt: String
//) : WebSocketMessage
//
//@Serializable
//data class RankingDto(
//    @SerialName("rank") val rank: Int,
//    @SerialName("player_id") val playerId: String,
//    @SerialName("nickname") val nickname: String,
//    @SerialName("score") val score: Int,
//    @SerialName("stats") val stats: StatsDto,
//)
//
//@Serializable
//data class StatsDto(
//    @SerialName("workout_time") val workoutTime: Int,
//    @SerialName("avg_bpm") val avgBpm: Int,
//    @SerialName("min_bpm") val minBpm: Int,
//    @SerialName("max_bpm") val maxBpm: Int,
//    @SerialName("time_in_zone") val timeInZone: Int,
//)
