package com.shinythinking.applepulser_android.domain.repository

import com.shinythinking.applepulser_android.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    fun getUserSession(): Flow<UserSession>

    suspend fun saveUserId(userId: String)
    suspend fun saveUserName(userName: String)
    suspend fun saveRoomId(roomId: String)
    suspend fun saveIsHost(isHost: Boolean)
    suspend fun savePlayerType(playerType: String)
    suspend fun saveUserSession(session: UserSession)
    suspend fun clearUserSession()
}