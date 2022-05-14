package com.safwachy.safechat.service

import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.model.MessageDetail
import com.safwachy.safechat.model.MessageModel
import com.safwachy.safechat.model.UserModel
import com.safwachy.safechat.repository.MessageRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

interface MessageService {
    fun postMessage(user: UserModel, messageBody: String) : MessageModel
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
}