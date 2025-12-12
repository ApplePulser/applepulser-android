package com.shinythinking.applepulser_android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResult(
    val playerId: String,
    val name: String,
    val rank: Int,
    val averageBpm: Double,
    val maxBpm: Int,
    val minBpm: Int,
)