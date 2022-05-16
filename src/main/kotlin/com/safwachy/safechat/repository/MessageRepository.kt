package com.safwachy.safechat.repository

import com.safwachy.safechat.model.MessageModel
import com.safwachy.safechat.model.MessageResult
import com.safwachy.safechat.repository.table.Message
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

interface MessageRepository {
    fun insertMessage(message: MessageModel)
    fun findAllByRoomId(roomId: UUID) : List<MessageResult>
    fun findAllRecentByRoomId(roomId: UUID, lastMessageDateTime: LocalDateTime) : List<MessageResult>
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

    override fun findAllByRoomId(roomId: UUID) : List<MessageResult> {
        val messages = mutableListOf<MessageResult>()

        val sql = """
            select 
                m.id as id,
                m.message_body as message_body,
                u.name as name,
                m.created_date as created_date 
            from message m
            left join public.user u on m.creator_id = u.id
            where m.room_id  =  '$roomId'
            order by created_date asc
        """.trimIndent()

        transaction {
            TransactionManager.current().exec(sql) {
                if (!it.isBeforeFirst) return@exec
                while (it.next()) {
                    messages.add(
                        MessageResult(
                            UUID.fromString(it.getString("id")),
                            it.getString("message_body"),
                            it.getString("name"),
                            it.getTimestamp("created_date").toLocalDateTime()
                        )
                    )
                }
            }
        }

        return messages
    }

    override fun findAllRecentByRoomId(roomId: UUID, lastMessageDateTime: LocalDateTime): List<MessageResult> {
        val messages = mutableListOf<MessageResult>()

        val sql = """
            select 
                m.id as id,
                m.message_body as message_body,
                u.name as name,
                m.created_date as created_date 
            from message m
            left join public.user u on m.creator_id = u.id
            where m.room_id  =  '$roomId' and created_date > ${lastMessageDateTime.toInstant(ZoneOffset.UTC)}
            order by created_date asc
        """.trimIndent()

        transaction {
            TransactionManager.current().exec(sql) {
                if (!it.isBeforeFirst) return@exec
                while (it.next()) {
                    messages.add(
                        MessageResult(
                            UUID.fromString(it.getString("id")),
                            it.getString("message_body"),
                            it.getString("name"),
                            it.getTimestamp("created_date").toLocalDateTime()
                        )
                    )
                }
            }
        }

        return messages
    }
}