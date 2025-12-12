package com.shinythinking.applepulser_android.data.dto

import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult
import com.shinythinking.applepulser_android.domain.model.PlayerStatus
import com.shinythinking.applepulser_android.domain.model.RoomInfo
import com.shinythinking.applepulser_android.domain.model.RoomStatus
import com.shinythinking.applepulser_android.domain.model.event.GameEvent
import com.shinythinking.applepulser_android.domain.model.event.RoomEvent

fun CreateRoomResponse.toDomain(): RoomInfo {
    return RoomInfo(
        roomId = roomId,
        roomCode = roomCode,
//        qrCode = qrCode,
        players = players.map { it.toDomain().copy(status = PlayerStatus.WAITING) },
        myPlayerId = players.first().playerId,
        maxPlayers = maxPlayers,
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
        isHost = isHost
    )
}

fun JoinRoomResponse.toDomain(): RoomInfo {
    return RoomInfo(
        myPlayerId = myPlayerId,
        roomId = roomId,
        roomCode = roomCode,
        status = when (status) {
            "waiting" -> RoomStatus.WAITING
            "playing" -> RoomStatus.PLAYING
            "finished" -> RoomStatus.FINISHED
            else -> RoomStatus.WAITING
        },
        players = players.map { it.toDomain() }
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
        players = emptyList(),
    )
}

fun PlayerJoinedMessage.toDomainEvent(): RoomEvent.PlayerJoined {
    return RoomEvent.PlayerJoined(
        player = player.toDomain(),
        currentCount = totalPlayers
    )
}

fun RoomPlayerSocketDto.toDomain(): Player {
    return Player(
        id = playerId,
        name = nickname,
        status = if (isReady == "true") PlayerStatus.READY else PlayerStatus.WAITING,
        isHost = isHost == "true"
    )
}

fun PlayerLeftMessage.toDomainEvent(): RoomEvent.PlayerLeft {
    return RoomEvent.PlayerLeft(
        playerId = playerId,
        nickname = nickname,
    )
}

fun PlayerLeftMessage.toDomainGameEvent(): GameEvent.PlayerLeft {
    return GameEvent.PlayerLeft(
        playerId = playerId,
        nickname = nickname,
    )
}

fun PlayerReadyMessage.toDomainEvent(): RoomEvent.PlayerReady {
    val isReady = isReady == "true"
    return RoomEvent.PlayerReady(
        playerId = playerId,
        isReady = isReady
    )
}

fun GameStartMessage.toDomainEvent(): RoomEvent.GameStarted {
    return RoomEvent.GameStarted(
        gameSetting = GameSetting(
            roomId = null,
            gameMode = GameMode.STEADY_BEAT,
            timeLimit = totalTime,
            bpmMax = maxBpm,
            bpmMin = minBpm,
            players = players.map { it.toDomain() }
        ),
    )
}

fun GamePlayerDto.toDomain(): Player {
    return Player(
        id = playerId,
        name = nickname,
        isHost = isHost == "true",
        bpm = bpm,
        deviation = diff,
        status = PlayerStatus.PLAYING
    )
}

fun ResultDto.toDomain(): PlayerResult {
    return PlayerResult(
        playerId = playerId,
        name = nickname,
        rank = rank,
        averageBpm = avgMae,
        maxBpm = maxBpm,
        minBpm = minBpm
    )
}

fun ErrorMessage.toDomainEvent(): RoomEvent.ErrorDelivered {
    return RoomEvent.ErrorDelivered(
        message = message
    )
}

fun ErrorMessage.toDomainGameEvent(): GameEvent.ErrorDelivered {
    return GameEvent.ErrorDelivered(
        message = message
    )
}

fun BpmUpdateMessage.toDomainEvent(): GameEvent.HeartbeatUpdate {
    return GameEvent.HeartbeatUpdate(
        players = playersRanking.mapIndexed { index, it ->
            it.toDomain().copy(rank = index + 1)
        }
    )
}


fun GameEndMessage.toDomainEvent(): GameEvent.GameEnded {
    return GameEvent.GameEnded(
        results = results.map { it.toDomain() }
    )
}


//fun RoomPlayerDto.toDomain(): Player {
//    return Player(
//        id = playerId,
//        name = nickname,
//        status = PlayerStatus.WAITING,
//        isHost = if(isHost == "true") true else false,
//    )
//}

//fun GamePlayerDto.toDomain(): Player {
//    return Player(
//        id = playerId,
//        name = nickname,
//        bpm = bpm,
//        rank = rank,
//        deviation = deviation,
//        status = PlayerStatus.PLAYING
//    )
//}
