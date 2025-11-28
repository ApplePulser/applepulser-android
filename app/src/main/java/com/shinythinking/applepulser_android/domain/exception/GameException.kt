package com.shinythinking.applepulser_android.domain.exception

sealed class GameException(message: String) : Exception(message)

class GeneralGameException(message: String) :
    GameException(message)

class GameNotStartedException :
    GameException("Game has not started yet")

class InvalidHeartRateException(rate: Int) :
    GameException("Invalid heart rate: $rate")