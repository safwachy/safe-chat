package com.safwachy.safechat.repository

import com.safwachy.safechat.model.MessageModel
import com.safwachy.safechat.repository.table.Message
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

interface MessageRepository {
    fun insertMessage(message: MessageModel)
}

@Repository
class MessageRepositoryImpl : MessageRepository {
    override fun insertMessage(message: MessageModel) {
        transaction {
            Message.insert {
                it[id] = message.id
                it[creatorId] = message.creatorId
                it[roomId] = message.roomId
                it[messageBody] = message.messageBody
                it[createdDate] = message.createdDate.toInstant(ZoneOffset.UTC)
            }
        }
    }
}