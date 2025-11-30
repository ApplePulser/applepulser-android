package com.shinythinking.applepulser_android.domain.repository

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {
    suspend fun connect(device: BluetoothDevice)
    fun disconnect()
    fun observeHeartRate(): Flow<Int>
    val connectionState: StateFlow<Boolean>
    suspend fun controlMotor(isOn: Boolean)
}