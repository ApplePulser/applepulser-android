package com.shinythinking.applepulser_android.domain.model.event

import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult

sealed interface GameEvent {
    data class HeartbeatUpdate(val players: List<Player>, val timestamp: String) : GameEvent
    data class GameEnded(val reason: String, val endedAt: String) : GameEvent
    data class GameResultReceived(val rankings: List<PlayerResult>, val mode: GameMode) : GameEvent
    data class Error(val message: String) : GameEvent
}