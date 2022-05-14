package com.safwachy.safechat.repository.table

import com.safwachy.safechat.model.MessageModel.Companion.MAX_MESSAGE_CHAR_LENGTH
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Message : Table() {
    val id = uuid("id").uniqueIndex()
    val creatorId = uuid("creator_id").references(User.id)
    val roomId = uuid("room_id").references(Room.id)
    val messageBody = varchar("message_body", MAX_MESSAGE_CHAR_LENGTH)
    val createdDate = timestamp("created_date")

    override val primaryKey = PrimaryKey(id)
}