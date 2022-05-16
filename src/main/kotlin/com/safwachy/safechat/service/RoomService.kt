package com.safwachy.safechat.service

import com.safwachy.safechat.helper.SecurityUtil
import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserModel
import com.safwachy.safechat.repository.RoomRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface RoomService {
    fun create(newRoomCode: String) : RoomModel
    fun findByCode(roomCode: String) : RoomModel
}

@Service
class RoomServiceImpl (
    private val roomRepository: RoomRepository
) : RoomService {
    override fun create(newRoomCode: String): RoomModel {
        val hashedRoomCode = SecurityUtil.sha256Hash(newRoomCode)
        val room = RoomModel(UUID.randomUUID(), hashedRoomCode, LocalDateTime.now())
        roomRepository.insertRoom(room)
        return room
    }

    override fun findByCode(roomCode: String): RoomModel {
        val hashedRoomCode = SecurityUtil.sha256Hash(roomCode)
        return roomRepository.findByCode(hashedRoomCode)
    }
}