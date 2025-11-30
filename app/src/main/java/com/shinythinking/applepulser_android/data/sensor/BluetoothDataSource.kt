package com.shinythinking.applepulser_android.data.sensor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothDataSource @Inject constructor() {

    companion object {
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val TAG = "BluetoothDataSource"
    }

    private var socket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private val _incomingBytes = MutableSharedFlow<ByteArray>(replay = 0)
    val incomingBytes = _incomingBytes.asSharedFlow()

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    @SuppressLint("MissingPermission")
    suspend fun connect(device: BluetoothDevice) = withContext(Dispatchers.IO) {
        try {
            if (_connectionState.value) disconnect()

            socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
            socket?.connect()

            inputStream = socket?.inputStream
            outputStream = socket?.outputStream
            _connectionState.emit(true)
            Log.d(TAG, "Connected to ${device.name}")

            val buffer = ByteArray(1024)
            while (true) {
                val bytesRead = inputStream?.read(buffer) ?: -1
                if (bytesRead == -1) break

                if (bytesRead > 0) {
                    val actualBytes = buffer.copyOf(bytesRead)
                    _incomingBytes.emit(actualBytes)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Connection failed or lost", e)
        } finally {
            disconnect()
        }
    }

    suspend fun send(bytes: ByteArray) = withContext(Dispatchers.IO) {
        try {
            outputStream?.write(bytes)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to send data", e)
            disconnect()
        }
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing socket", e)
        }
        socket = null
        inputStream = null
        outputStream = null
        _connectionState.tryEmit(false)
    }
}