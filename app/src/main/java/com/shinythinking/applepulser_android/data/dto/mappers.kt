package com.shinythinking.applepulser_android.data.dto

import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.RoomStatus
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent

fun CreateRoomResponse.toDomain(): RoomInfo {
    return RoomInfo(
        roomId = roomId,
        roomCode = roomCode,
        qrCode = qrCode,
        players = listOf(host.toDomain()),
        status = RoomStatus.WAITING,
    )
}

fun GetRoomInfoResponse.toDomain(): RoomInfo {
    return RoomInfo(
        roomId = roomId,
        roomCode = roomCode,
        status = when (status) {
            "waiting" -> RoomStatus.WAITING
            "playing" -> RoomStatus.PLAYING
            "finished" -> RoomStatus.FINISHED
            else -> RoomStatus.WAITING
        },
        maxPlayers = maxPlayers,
        players = players.map { it.toDomain() }
    )
}

fun RoomHostDto.toDomain() = Player(
    id = playerId,
    name = nickname,
)

fun RoomPlayerDto.toDomain(): Player {
    return Player(
        id = playerId,
        name = nickname,
        status = when (status) {
            "ready" -> PlayerStatus.READY
            "playing" -> PlayerStatus.PLAYING
            "finished" -> PlayerStatus.FINISHED
            else -> PlayerStatus.WAITING
        },
        isHost = isHost,
    )
}

fun JoinRoomResponse.toDomain(): RoomInfo {
    return RoomInfo(
        roomId = room.roomId,
        roomCode = room.roomCode,
        status = when (room.status) {
            "waiting" -> RoomStatus.WAITING
            "playing" -> RoomStatus.PLAYING
            "finished" -> RoomStatus.FINISHED
            else -> RoomStatus.WAITING
        },
    )
}

fun StartGameResponse.toDomain(): GameSetting {
    return GameSetting(
        roomId = roomId,
        gameMode = when (gameSettings.mode) {
            "steady_beat" -> GameMode.STEADY_BEAT
            else -> GameMode.STEADY_BEAT
        },
        timeLimit = gameSettings.timeLimit,
        bpmMax = gameSettings.bpmMax,
        bpmMin = gameSettings.bpmMin,
        players = emptyList(), //todo players.map { it.toDomain() }
    )
}

fun PlayerJoinedMessage.toDomainEvent(): RoomEvent.PlayerJoined {
    return RoomEvent.PlayerJoined(
        player = player.toDomain(),
        currentCount = totalPlayers
    )
}

fun PlayerLeftMessage.toDomainEvent(): RoomEvent.PlayerLeft {
    return RoomEvent.PlayerLeft(
        playerId = playerId,
        nickname = nickname,
        currentCount = totalPlayers
    )
}

fun GameStartedMessage.toDomainEvent(): RoomEvent.GameStarted {
    return RoomEvent.GameStarted(
        mode = when (mode) {
            "steady_beat" -> GameMode.STEADY_BEAT
            else -> GameMode.PULSE_RUSH
        },
        startedAt = startedAt
    )
}

fun GamePlayerDto.toDomain(): Player {
    return Player(
        id = playerId,
        name = nickname,
        bpm = bpm,
        rank = rank,
        deviation = deviation,
        status = PlayerStatus.PLAYING
    )
}

fun RankingDto.toDomain(): PlayerResult {
    return PlayerResult(
        player = Player(
            id = playerId,
            name = nickname,
            status = PlayerStatus.FINISHED,
            rank = rank,
        ),
        rank = rank,
        score = score,
        workoutTime = stats.workoutTime,
        averageBpm = stats.avgBpm,
        maxBpm = stats.maxBpm,
        minBpm = stats.minBpm,
        timeInZone = stats.timeInZone
    )
}