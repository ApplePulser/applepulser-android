package com.shinythinking.applepulser_android.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.shinythinking.applepulser_android.data.sensor.BluetoothDataSource
import com.shinythinking.applepulser_android.domain.repository.HeartbeatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartbeatRepositoryImpl @Inject constructor(
    private val dataSource: BluetoothDataSource,
    @param:ApplicationContext private val context: Context
) : HeartbeatRepository {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager
        bluetoothManager?.adapter
    }

    companion object {
        private const val TAG = "BluetoothRepository"
    }

    override suspend fun connect(deviceAddress: String) {
        val adapter = bluetoothAdapter
        if (adapter == null) {
            Log.e(TAG, "Bluetooth not supported")
            return
        }

        try {
            val device = adapter.getRemoteDevice(deviceAddress)
            Log.d(TAG, "Connecting to address: $deviceAddress")
            dataSource.connect(device)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid address: $deviceAddress", e)
        }
    }

    override fun disconnect() {
        dataSource.disconnect()
    }

    override fun observeHeartRate(): Flow<Int> = flow {
        val localBuffer = StringBuilder()

        dataSource.incomingBytes.collect { bytes ->
            val chunk = String(bytes, Charsets.US_ASCII)

            localBuffer.append(chunk)

            var newlineIndex = localBuffer.indexOf('\n')
            while (newlineIndex != -1) {
                val rawMessage = localBuffer.substring(0, newlineIndex).trim()

                localBuffer.delete(0, newlineIndex + 1)


                val bpm = rawMessage.toIntOrNull()

                if (bpm != null && bpm in 30..250) {
                    emit(bpm)
                } else {
                    if (rawMessage.isNotEmpty()) {
                        Log.w(TAG, "Ignored invalid data: $rawMessage")
                    }
                }

                newlineIndex = localBuffer.indexOf('\n')
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun getPairedDevices(): List<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<List<BluetoothDevice>> = callbackFlow {
        val foundDevices = mutableSetOf<BluetoothDevice>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device =
                            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            if (!it.name.isNullOrBlank()) {
                                foundDevices.add(it)
                                trySend(foundDevices.toList())
                            }
                        }
                    }
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        bluetoothAdapter?.startDiscovery()
        awaitClose {
            bluetoothAdapter?.cancelDiscovery()
            context.unregisterReceiver(receiver)
        }
    }
}