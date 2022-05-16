package com.safwachy.safechat.service

import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.model.*
import com.safwachy.safechat.repository.MessageRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

interface MessageService {
    fun postMessage(user: UserModel, messageBody: String) : MessageModel
    fun getMessage(room: RoomModel, lastMessageDateTime: LocalDateTime?) : List<MessageResult>
}

@Service
class MessageServiceImpl (
    private val messageRepository: MessageRepository
) : MessageService {
    override fun postMessage(user: UserModel, messageBody: String) : MessageModel {
        if (messageBody.isEmpty() || messageBody.length > MessageModel.MAX_MESSAGE_CHAR_LENGTH) {
            throw ValidationException("Invalid message length, must be 500 characters or less and non-empty")
        }
        val message = MessageModel(UUID.randomUUID(), user.id!!, user.roomId!!, messageBody, LocalDateTime.now())
        messageRepository.insertMessage(message)
        return message
    }

    override fun getMessage(room: RoomModel, lastMessageDateTime: LocalDateTime?): List<MessageResult> {
        if (room.id == null) {
            throw IllegalArgumentException("Room UUID cannot be null")
        }
        if (lastMessageDateTime == null) {
            return messageRepository.findAllByRoomId(room.id)
        }
        return messageRepository.findAllRecentByRoomId(room.id, lastMessageDateTime)
    }
}