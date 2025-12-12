package com.shinythinking.applepulser_android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSetting(
    val roomId: String?,
    val gameMode: GameMode,
    val timeLimit: Int,
    val bpmMin: Int,
    val bpmMax: Int,
    val players: List<Player>
)