package com.shinythinking.applepulser_android.di

import android.util.Log
import com.shinythinking.applepulser_android.data.api.ApiDataSource
import com.shinythinking.applepulser_android.data.network.SocketDataSource
import com.shinythinking.applepulser_android.data.repository.BluetoothRepositoryImpl
import com.shinythinking.applepulser_android.data.repository.GameRepositoryImpl
import com.shinythinking.applepulser_android.data.repository.RoomRepositoryImpl
import com.shinythinking.applepulser_android.domain.repository.BluetoothRepository
import com.shinythinking.applepulser_android.domain.repository.GameRepository
import com.shinythinking.applepulser_android.domain.repository.RoomRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(json)
            }

            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(json)
                pingInterval = 20_000.milliseconds
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorClient", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiDataSource(
        client: HttpClient
    ): ApiDataSource {
        return ApiDataSource(client)
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(
        client: HttpClient,
        json: Json
    ): SocketDataSource {
        return SocketDataSource(client, json)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BluetoothModule {

        @Binds
        @Singleton
        abstract fun bindBluetoothRepository(
            impl: BluetoothRepositoryImpl
        ): BluetoothRepository
    }

    @Provides
    @Singleton
    fun provideRoomRepository(
        apiDataSource: ApiDataSource,
        socketDataSource: SocketDataSource
    ): RoomRepository {
        return RoomRepositoryImpl(apiDataSource, socketDataSource)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        socketDataSource: SocketDataSource
    ): GameRepository {
        return GameRepositoryImpl(socketDataSource)
    }
}
