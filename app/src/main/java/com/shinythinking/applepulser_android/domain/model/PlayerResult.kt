package com.shinythinking.applepulser_android.domain.model

data class PlayerResult(
    val player: Player,
    val rank: Int,
    val averageBpm: Int,
    val peakBpm: Int,
    val timeInRange: Int
)