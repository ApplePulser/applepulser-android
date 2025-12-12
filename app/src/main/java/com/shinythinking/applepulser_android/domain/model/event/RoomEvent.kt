package com.shinythinking.applepulser_android.domain.model.event

import com.shinythinking.applepulser_android.domain.model.GameSetting
import com.shinythinking.applepulser_android.domain.model.Player

sealed interface RoomEvent {
    data class PlayerReady(val playerId: String, val isReady: Boolean) : RoomEvent
    data class GameStarted(val gameSetting: GameSetting) : RoomEvent
    data class PlayerJoined(val player: Player, val currentCount: Int) : RoomEvent
    data class PlayerLeft(val playerId: String, val nickname: String) : RoomEvent
    data class ErrorDelivered(val message: String) : RoomEvent
}