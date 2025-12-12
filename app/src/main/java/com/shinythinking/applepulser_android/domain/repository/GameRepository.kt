package com.shinythinking.applepulser_android.domain.repository

import com.shinythinking.applepulser_android.domain.model.event.GameEvent
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun sendHeartRate(playerId: String, bpm: Int)
    fun observeGameEvents(): Flow<GameEvent>
}
