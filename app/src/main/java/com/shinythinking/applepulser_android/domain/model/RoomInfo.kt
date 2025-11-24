package com.shinythinking.applepulser_android.domain.model

data class RoomInfo(
    val roomCode: String,
    val hostId: String,
    val players: List<Player> = emptyList(),
    val gameMode: GameMode? = null,
    val maxPlayers: Int = 4,
    val isGameStarted: Boolean = false
) {
    val currentPlayerCount: Int
        get() = players.size

    val isFull: Boolean
        get() = players.size >= maxPlayers

    val canStart: Boolean
        get() = players.size >= 2 && gameMode != null && players.all { it.isReady }
}