package com.shinythinking.applepulser_android.domain.usecase

import android.util.Log
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.HeartbeatRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import javax.inject.Inject

@OptIn(FlowPreview::class)
class SyncHeartbeatUseCase @Inject constructor(
    private val heartbeatRepository: HeartbeatRepository,
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(playerId: String) {
        heartbeatRepository.observeHeartRate()
            .sample(1000L)
            .onEach { bpm ->
                if (bpm > 0) {
                    try {
                        gameRepository.sendHeartRate(playerId, bpm)
                    } catch (e: Exception) {
                        Log.e("SyncUseCase", "Failed to send BPM: $bpm", e)
                    }
                }
            }
            .catch { e ->
                Log.e("SyncUseCase", "Bluetooth stream error", e)
            }
            .collect()
    }
}