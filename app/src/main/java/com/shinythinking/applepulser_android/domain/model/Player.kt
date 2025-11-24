package com.shinythinking.applepulser_android.domain.model

data class Player(
    val id: String,
    val name: String,
    val heartRate: Int = 0,
    val rank: Int = 0,
    val colorType: PlayerType = PlayerType.RED,
    val isHost: Boolean = false,
    val isReady: Boolean = false
)

enum class PlayerType {
    RED,
    YELLOW,
    GREEN,
    WHITE;

    fun getCharacterImage(): Int = when (this) {
        RED -> com.shinythinking.applepulser_android.R.drawable.ic_red_apple
        YELLOW -> com.shinythinking.applepulser_android.R.drawable.ic_yellow_apple
        GREEN -> com.shinythinking.applepulser_android.R.drawable.ic_green_apple
        WHITE -> com.shinythinking.applepulser_android.R.drawable.ic_white_apple
    }

    fun toColorName(): String = when (this) {
        RED -> "Red Apple"
        YELLOW -> "Yellow Apple"
        GREEN -> "Green Apple"
        WHITE -> "White Apple"
    }
}