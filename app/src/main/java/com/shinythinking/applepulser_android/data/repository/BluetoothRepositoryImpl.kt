package com.shinythinking.applepulser_android.data.repository

import android.bluetooth.BluetoothDevice
import android.util.Log
import com.shinythinking.applepulser_android.data.sensor.BluetoothDataSource
import com.shinythinking.applepulser_android.domain.repository.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothRepositoryImpl @Inject constructor(
    private val dataSource: BluetoothDataSource
) : BluetoothRepository {

    companion object {
        private const val TAG = "BluetoothRepository"
    }

    private val buffer = StringBuilder()

    override val connectionState: StateFlow<Boolean> = dataSource.connectionState

    override suspend fun connect(device: BluetoothDevice) {
        synchronized(buffer) {
            buffer.setLength(0)
        }
        dataSource.connect(device)
    }

    override fun disconnect() {
        dataSource.disconnect()
    }

    override fun observeHeartRate(): Flow<Int> {
        return dataSource.incomingBytes
            .map { bytes ->
                val chunk = String(bytes, Charsets.US_ASCII)

                val bpmList = mutableListOf<Int>()

                synchronized(buffer) {
                    buffer.append(chunk)

                    var newlineIndex = buffer.indexOf('\n')
                    while (newlineIndex != -1) {
                        val rawMessage = buffer.substring(0, newlineIndex).trim()

                        buffer.delete(0, newlineIndex + 1)

                        val bpm = rawMessage.toIntOrNull()
                        if (bpm != null && bpm in 30..250) {
                            bpmList.add(bpm)
                        } else {
                            if (rawMessage.isNotEmpty()) {
                                Log.w(TAG, "Ignored invalid data: $rawMessage")
                            }
                        }

                        newlineIndex = buffer.indexOf('\n')
                    }
                }
                bpmList
            }
            .flatMapIterable { it }
    }

    override suspend fun controlMotor(isOn: Boolean) {
        val command = if (isOn) "1" else "0"
        try {
            dataSource.send(command.toByteArray(Charsets.US_ASCII))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send motor command", e)
        }
    }
}

private fun <T> Flow<List<T>>.flatMapIterable(transform: (List<T>) -> Iterable<T>): Flow<T> =
    kotlinx.coroutines.flow.flow {
        collect { list ->
            transform(list).forEach { emit(it) }
        }
    }