package com.shinythinking.applepulser_android.domain.exception

sealed class RoomException(message: String) : Exception(message)

class GeneralRoomException(message: String) :
    RoomException(message)

class RoomNotFoundException(roomCode: String) :
    RoomException("Room not found: $roomCode")

class RoomFullException :
    RoomException("Room is full")

class NotHostException :
    RoomException("Only host can perform this action")

class InvalidRoomCodeException :
    RoomException("Invalid room code format")