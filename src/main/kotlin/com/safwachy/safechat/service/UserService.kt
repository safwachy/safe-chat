package com.safwachy.safechat.service

import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserModel
import com.safwachy.safechat.repository.RoomRepository
import com.safwachy.safechat.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface UserService {
    fun create(userName: String, roomCode: String?) : UserModel
    fun updateRoom(user: UserModel, room: RoomModel)
}

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository
): UserService {
    override fun create(userName: String, roomCode: String?) : UserModel {
        if (userName.isEmpty() || userName.length > UserModel.MAX_USER_NAME_LENGTH) {
            throw ValidationException("Invalid length for user name, must be 50 characters or less")
        }
        if (roomCode != null) {
            val room = roomRepository.findByCode(roomCode)
            if (userRepository.findByUserNameAndRoomId(userName, room.id!!).id != null) throw ValidationException("A user with that name already exists, please pick a different name")
        }
        val user = UserModel(UUID.randomUUID(), null, userName, LocalDateTime.now())
        userRepository.createUser(user)
        return user
    }

    override fun updateRoom(user: UserModel, room: RoomModel) {
        if (room.id == null && user.id == null) throw IllegalArgumentException("Room UUID cannot be null")
        user.roomId = room.id
        userRepository.updateUserRoomId(user.id!!, room.id!!)
    }
}