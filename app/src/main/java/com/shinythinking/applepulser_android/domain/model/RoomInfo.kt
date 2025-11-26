package com.shinythinking.applepulser_android.domain.model

data class RoomInfo(
    val roomId: String,
    val roomCode: String,
    val qrCode: String? = null,
    val status: RoomStatus,
    val maxPlayers: Int = 4,
    val players: List<Player> = emptyList(),
) {
    val currentPlayerCount: Int
        get() = players.size

    val isFull: Boolean
        get() = players.size >= maxPlayers

}

enum class RoomStatus {
    WAITING, PLAYING, FINISHED
}