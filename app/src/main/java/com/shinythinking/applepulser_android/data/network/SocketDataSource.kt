package com.shinythinking.applepulser_android.data.network

import android.util.Log
import com.shinythinking.applepulser_android.data.dto.WebSocketMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketDataSource @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) {
    companion object {
        private const val TAG = "SharedSocketDataSource"

        private const val WS_HOST = "10.0.2.2"
        private const val WS_PORT = 8000
    }

    private val _connectionState =
        MutableStateFlow<SocketConnectionState>(SocketConnectionState.Disconnected)
    val connectionState: StateFlow<SocketConnectionState> = _connectionState.asStateFlow()

    private val _incomingMessages = MutableSharedFlow<WebSocketMessage>(replay = 0)
    val incomingMessages: SharedFlow<WebSocketMessage> = _incomingMessages.asSharedFlow()

    private val _errors = MutableSharedFlow<WebSocketError>()
    val errors: SharedFlow<WebSocketError> = _errors.asSharedFlow()

    private var sessionJob: Job? = null

    private var currentSession: WebSocketSession? = null

    suspend fun connect(roomId: String) {
        if (_connectionState.value is SocketConnectionState.Connected) return

        sessionJob?.cancel()
        sessionJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                _connectionState.value = SocketConnectionState.Connecting
                val path = "/ws/room/$roomId/"

                Log.d(TAG, "Connecting to ws://$WS_HOST:$WS_PORT$path")

                client.webSocket(host = WS_HOST, port = WS_PORT, path = path) {
                    currentSession = this
                    _connectionState.value = SocketConnectionState.Connected
                    Log.d(TAG, "WebSocket Connected!")

                    for (frame in incoming) {
                        try {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                Log.v(TAG, "RX: $text") // 로그 확인용

                                val message = json.decodeFromString<WebSocketMessage>(text)
                                _incomingMessages.emit(message)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Message parse error: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "WebSocket Error", e)
                _connectionState.value = SocketConnectionState.Error(e)
                _errors.emit(WebSocketError.ConnectionFailed(e.message ?: "Connection failed"))
            } finally {
                Log.d(TAG, "WebSocket Disconnected")
                currentSession = null
                _connectionState.value = SocketConnectionState.Disconnected
            }
        }
    }

    suspend fun sendMessage(message: WebSocketMessage) {
        val session = currentSession
        if (session == null || !session.isActive) {
            Log.w(TAG, "Cannot send message: Socket not connected")
            _errors.emit(WebSocketError.NotConnected)
            return
        }

        try {
            val jsonString = json.encodeToString(WebSocketMessage.serializer(), message)
            Log.v(TAG, "TX: $jsonString")
            session.send(Frame.Text(jsonString))
        } catch (e: Exception) {
            Log.e(TAG, "Send failed", e)
            _errors.emit(WebSocketError.SendFailed(e.message ?: "Send failed"))
        }
    }

    suspend fun disconnect() {
        if (currentSession != null) {
            currentSession?.close(CloseReason(CloseReason.Codes.NORMAL, "Client closed"))
            currentSession = null
        }
        sessionJob?.cancel()
        _connectionState.value = SocketConnectionState.Disconnected
    }
}
