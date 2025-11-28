package com.shinythinking.applepulser_android.data.network

sealed interface SocketConnectionState {
    data object Disconnected : SocketConnectionState
    data object Connecting : SocketConnectionState
    data object Connected : SocketConnectionState
    data object Reconnecting : SocketConnectionState
    data class Error(val throwable: Throwable) : SocketConnectionState
}