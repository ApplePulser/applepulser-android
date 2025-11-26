package com.shinythinking.applepulser_android.domain.model

data class GameSetting(
    val roomId: String,
    val gameMode: GameMode,
    val timeLimit: Int,
    val bpmMin: Int,
    val bpmMax: Int,
    val players: List<Player>
) {
    val canStart: Boolean
        get() = players.size >= 2 && players.all { it.status == PlayerStatus.READY }
}