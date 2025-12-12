package com.shinythinking.applepulser_android.domain.model.event

import com.shinythinking.applepulser_android.domain.model.Player
import com.shinythinking.applepulser_android.domain.model.PlayerResult

sealed interface GameEvent {
    data class HeartbeatUpdate(val players: List<Player>) : GameEvent
    data class GameEnded(val results: List<PlayerResult>) : GameEvent
    data class PlayerLeft(val playerId: String, val nickname: String) : GameEvent
    data class ErrorDelivered(val message: String) : GameEvent
}