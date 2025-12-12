package com.shinythinking.applepulser_android.domain.model

data class RoomInfo(
    val roomId: String,
    val roomCode: String,
    val status: RoomStatus,
//    val qrCode: String? = null,
    val myPlayerId: String? = null,
    val maxPlayers: Int = 4,
    val players: List<Player> = emptyList(),
) {
    val currentPlayerCount: Int
        get() = players.size

    val isFull: Boolean
        get() = players.size >= maxPlayers

    val canStart: Boolean
        get() = players.size >= 2 && players.all { it.status == PlayerStatus.READY }
}

enum class RoomStatus {
    WAITING, PLAYING, FINISHED
}