package com.shinythinking.applepulser_android.data.repository

import com.shinythinking.applepulser_android.data.local.datastore.UserSessionLocalDataSource
import com.shinythinking.applepulser_android.data.local.datastore.UserSessionLocalDataSourceImpl
import com.shinythinking.applepulser_android.domain.model.UserSession
import com.shinythinking.applepulser_android.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val localDataSource: UserSessionLocalDataSource
) : UserSessionRepository {

    override fun getUserSession(): Flow<UserSession> {
        return localDataSource.getPreferences()
            .map { preferences ->
                UserSession(
                    userId = preferences[UserSessionLocalDataSourceImpl.USER_ID_KEY],
                    userName = preferences[UserSessionLocalDataSourceImpl.USER_NAME_KEY],
                    roomId = preferences[UserSessionLocalDataSourceImpl.ROOM_ID_KEY],
                    isHost = preferences[UserSessionLocalDataSourceImpl.IS_HOST_KEY] ?: false,
                    playerType = preferences[UserSessionLocalDataSourceImpl.PLAYER_TYPE_KEY]
                )
            }
    }

    override suspend fun saveUserId(userId: String) {
        localDataSource.saveString(
            UserSessionLocalDataSourceImpl.USER_ID_KEY,
            userId
        )
    }

    override suspend fun saveUserName(userName: String) {
        localDataSource.saveString(
            UserSessionLocalDataSourceImpl.USER_NAME_KEY,
            userName
        )
    }

    override suspend fun saveRoomId(roomId: String) {
        localDataSource.saveString(
            UserSessionLocalDataSourceImpl.ROOM_ID_KEY,
            roomId
        )
    }

    override suspend fun saveIsHost(isHost: Boolean) {
        localDataSource.saveBoolean(
            UserSessionLocalDataSourceImpl.IS_HOST_KEY,
            isHost
        )
    }

    override suspend fun savePlayerType(playerType: String) {
        localDataSource.saveString(
            UserSessionLocalDataSourceImpl.PLAYER_TYPE_KEY,
            playerType
        )
    }

    override suspend fun saveUserSession(session: UserSession) {
        session.userId?.let { saveUserId(it) }
        session.userName?.let { saveUserName(it) }
        session.roomId?.let { saveRoomId(it) }
        saveIsHost(session.isHost)
        session.playerType?.let { savePlayerType(it) }
    }

    override suspend fun clearUserSession() {
        localDataSource.clear()
    }
}