package com.shinythinking.applepulser_android.data.repository

import android.util.Log
import com.shinythinking.applepulser_android.data.dto.BpmUpdateMessage
import com.shinythinking.applepulser_android.data.dto.ErrorMessage
import com.shinythinking.applepulser_android.data.dto.GameEndMessage
import com.shinythinking.applepulser_android.data.dto.HeartbeatMessage
import com.shinythinking.applepulser_android.data.dto.PlayerLeftMessage
import com.shinythinking.applepulser_android.data.dto.toDomainEvent
import com.shinythinking.applepulser_android.data.dto.toDomainGameEvent
import com.shinythinking.applepulser_android.data.network.SocketDataSource
import com.shinythinking.applepulser_android.domain.model.event.GameEvent
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val socketDataSource: SocketDataSource
) : GameRepository {
    override suspend fun sendHeartRate(playerId: String, bpm: Int) {
        val message = HeartbeatMessage(
            playerId = playerId,
            bpm = bpm,
        )

        socketDataSource.sendMessage(message)
    }

    override fun observeGameEvents(): Flow<GameEvent> {
        return socketDataSource.incomingMessages
            .mapNotNull { message ->
                Log.d("GameRepositoryImpl", "observeGameEvents:${message}")
                when (message) {
                    is GameEndMessage -> message.toDomainEvent()
                    is ErrorMessage -> message.toDomainGameEvent()
                    is BpmUpdateMessage -> message.toDomainEvent()
                    is PlayerLeftMessage -> message.toDomainGameEvent()
                    else -> null
                }
            }
    }
}
