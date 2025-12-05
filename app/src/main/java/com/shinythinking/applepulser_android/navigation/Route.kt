package com.shinythinking.applepulser_android.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object LaunchRoom : Route

    @Serializable
    data object EnterRoomByCode : Route

    @Serializable
    data class HostRoom(
        val playerId: String,
        val roomId: String,
    ) : Route

    @Serializable
    data class ClientRoom(
        val playerId: String,
        val roomId: String,
    ) : Route

    @Serializable
    data class GameSettings(
        val playerId: String,
        val roomId: String,
    ) : Route

    @Serializable
    data class SteadyBeatGame(
        val playerId: String,
        val roomId: String,
        val settingJson: String,
        val deviceAddress: String
    ) : Route

    @Serializable
    data class GameResult(
        val playerId: String,
        val roomId: String,
        val resultJson: String
    ) : Route


    @Serializable
    data class Record(
        val playerId: String,
        val roomId: String,
        val resultJson: String
    ) : Route
}