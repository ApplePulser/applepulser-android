package com.shinythinking.applepulser_android.data.repository

import com.shinythinking.applepulser_android.data.dto.BroadcastHeartbeatMessage
import com.shinythinking.applepulser_android.data.dto.ErrorMessage
import com.shinythinking.applepulser_android.data.dto.GameEndedMessage
import com.shinythinking.applepulser_android.data.dto.GameResultMessage
import com.shinythinking.applepulser_android.data.dto.SendHeartbeatMessage
import com.shinythinking.applepulser_android.data.dto.toDomain
import com.shinythinking.applepulser_android.data.network.SocketDataSource
import com.shinythinking.applepulser_android.domain.model.GameMode
import com.shinythinking.applepulser_android.domain.model.event.GameEvent
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val socketDataSource: SocketDataSource
) : GameRepository {
    override suspend fun sendHeartRate(playerId: String, bpm: Int) {
        val currentTimestamp = Instant.now().toString()

        val message = SendHeartbeatMessage(
            playerId = playerId,
            bpm = bpm,
            timestamp = currentTimestamp
        )

        socketDataSource.sendMessage(message)
    }

    override fun observeGameEvents(): Flow<GameEvent> {
        return socketDataSource.incomingMessages
            .mapNotNull { message ->
                when (message) {
                    is BroadcastHeartbeatMessage -> {
                        GameEvent.HeartbeatUpdate(
                            players = message.players.map { it.toDomain() },
                            timestamp = message.timestamp
                        )
                    }

                    is GameEndedMessage -> {
                        GameEvent.GameEnded(
                            reason = message.reason,
                            endedAt = message.endedAt
                        )
                    }

                    is GameResultMessage -> {
                        GameEvent.GameResultReceived(
                            rankings = message.rankings.map { it.toDomain() },
                            mode = when (message.mode) {
                                "steady_beat" -> GameMode.STEADY_BEAT
                                "pulse_rush" -> GameMode.PULSE_RUSH
                                else -> GameMode.STEADY_BEAT
                            },
                        )
                    }

                    is ErrorMessage -> {
                        GameEvent.Error(message.message)
                    }

                    else -> null
                }
            }
    }
}
