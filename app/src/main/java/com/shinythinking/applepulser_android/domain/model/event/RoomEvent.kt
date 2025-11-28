package com.shinythinking.applepulser_android.domain.model.event

import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.Player

sealed interface RoomEvent {
    data class PlayerJoined(val player: Player, val currentCount: Int) : RoomEvent
    data class PlayerLeft(val playerId: String, val nickname: String, val currentCount: Int) :
        RoomEvent

    data class GameStarted(val mode: GameMode, val startedAt: String) : RoomEvent
    data class Error(val message: String) : RoomEvent
}