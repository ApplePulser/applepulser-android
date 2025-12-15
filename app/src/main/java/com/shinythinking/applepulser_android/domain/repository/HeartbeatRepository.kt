package com.shinythinking.applepulser_android.domain.repository

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface HeartbeatRepository {
    suspend fun connect(deviceAddress: String)
    fun disconnect()
    fun observeHeartRate(): Flow<Int>
    fun getPairedDevices(): List<BluetoothDevice>
    fun scanDevices(): Flow<List<BluetoothDevice>>
}