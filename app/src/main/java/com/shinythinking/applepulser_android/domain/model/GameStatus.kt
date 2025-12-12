package com.shinythinking.applepulser_android.domain.model

data class GameStatus(
    val currentHeartRate: Int?,
    val currentRank: Int?,
    val deviationFromTarget: Int?,
    val totalTime: Int,
    val players: List<Player>,
    val limit: Limit,
    val elapsedTime: Int = 0,
) {
    val remainingTime: Int
        get() = totalTime - elapsedTime

    val progress: Float
        get() = elapsedTime.toFloat() / totalTime.toFloat()

    val elapsedTimeFormatted: String
        get() {
            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    val totalTimeFormatted: String
        get() {
            val minutes = totalTime / 60
            val seconds = totalTime % 60
            return "%02d:%02d".format(minutes, seconds)
        }
}

data class Limit(
    val min: Int,
    val max: Int
)