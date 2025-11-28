package com.shinythinking.applepulser_android.data.network

sealed interface WebSocketError {
    data class ConnectionFailed(val message: String) : WebSocketError
    object NotConnected : WebSocketError
    data class SendFailed(val message: String) : WebSocketError
    data class ParseFailed(val message: String) : WebSocketError
    object ReconnectFailed : WebSocketError
}