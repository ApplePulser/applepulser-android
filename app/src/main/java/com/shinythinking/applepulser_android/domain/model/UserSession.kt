package com.shinythinking.applepulser_android.domain.model

data class UserSession(
    val userId: String?,
    val userName: String?,
    val roomId: String?,
    val isHost: Boolean,
    val playerType: String?
) {
    companion object {
        val Empty = UserSession(
            userId = null,
            userName = null,
            roomId = null,
            isHost = false,
            playerType = null
        )
    }

    val isLoggedIn: Boolean
        get() = userId != null

    val isInRoom: Boolean
        get() = roomId != null
}