package com.safwachy.safechat.service

import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.helper.SecurityUtil
import com.safwachy.safechat.model.*
import com.safwachy.safechat.repository.MessageRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

interface MessageService {
    fun postMessage(user: UserModel, roomCode: String, messageBody: String)
    fun getMessage(room: RoomModel, lastMessageDateTime: LocalDateTime?) : List<MessageResult>
}

@Service
class MessageServiceImpl (
    private val messageRepository: MessageRepository
) : MessageService {
    override fun postMessage(user: UserModel, roomCode: String, messageBody: String) {
        if (messageBody.isEmpty() || messageBody.length > MessageModel.MAX_MESSAGE_CHAR_LENGTH) {
            throw ValidationException("Invalid message length, must be 500 characters or less and non-empty")
        }

        // Encrypt message before saving in db
        val secretKey = SecurityUtil.generateSecretKey(roomCode)
        val encryptedMessage = SecurityUtil.encrypt(messageBody, secretKey)

        // save message
        val message = MessageModel(UUID.randomUUID(), user.id!!, user.roomId!!, encryptedMessage, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        messageRepository.insertMessage(message)
    }

    override fun getMessage(room: RoomModel, lastMessageDateTime: LocalDateTime?): List<MessageResult> {
        if (room.id == null || room.roomCode == null) {
            throw IllegalArgumentException("Room cannot be null")
        }

        // Fetch all messages in room or only recent message in room
        val messages = if (lastMessageDateTime == null) {
            messageRepository.findAllByRoomId(room.id)
        } else {
            messageRepository.findAllRecentByRoomId(room.id, lastMessageDateTime)
        }

        // Decrypt all messages
        if (messages.isNotEmpty()) {
            val secretKey = SecurityUtil.generateSecretKey(room.roomCode!!)
            for (message in messages) {
                message.message = SecurityUtil.decrypt(message.message, secretKey)
            }
        }

        return messages
    }
}