package com.shinythinking.applepulser_android.domain.model

data class PlayerResult(
    val player: Player,
    val rank: Int,
    val score: Int,
    val workoutTime: Int,
    val averageBpm: Int,
    val maxBpm: Int,
    val minBpm: Int,
    val timeInZone: Int
)