package com.shinythinking.applepulser_android.domain.model

data class GameResult(
    val rankings: List<PlayerResult>,
    val gameMode: GameMode,
    val gameDuration: Int
)