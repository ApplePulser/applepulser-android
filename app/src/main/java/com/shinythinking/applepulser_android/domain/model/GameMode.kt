package com.shinythinking.applepulser_android.domain.model

enum class GameMode {
    STEADY_BEAT,
    PULSE_RUSH;

    val title: String
        get() = when (this) {
            STEADY_BEAT -> "Steady Beat"
            PULSE_RUSH -> "Pulse Rush"
        }

    val description: String
        get() = when (this) {
            STEADY_BEAT -> "Consistency is key\nHold your pulse in range\nto climb the leaderboard"
            PULSE_RUSH -> "Push your limits\nCompete to hit\nthe highest heart rate."
        }
}