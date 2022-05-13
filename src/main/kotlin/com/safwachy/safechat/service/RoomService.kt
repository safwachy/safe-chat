package com.safwachy.safechat.service

import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserModel
import com.safwachy.safechat.repository.RoomRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface RoomService {
    fun create() : RoomModel
    fun addUser(room: RoomModel, user: UserModel)
    fun findByCode(roomCode: String) : RoomModel
}

@Service
class RoomServiceImpl (
    private val roomRepository: RoomRepository
) : RoomService {
    override fun create(): RoomModel {
        val room = RoomModel(UUID.randomUUID(), RandomStringUtils.randomAlphanumeric(RoomModel.ROOM_CODE_LENGTH), LocalDateTime.now())
        roomRepository.createRoom(room)
        return room
    }

    override fun addUser(room: RoomModel, user: UserModel) {
        user.roomId = room.id
    }

    override fun findByCode(roomCode: String): RoomModel {
        return roomRepository.findByCode(roomCode)
    }
}