package com.safwachy.safechat.repository.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Message : Table() {
    val id = uuid("id")
    val creatorId = uuid("creator_id").references(User.id)
    val roomId = uuid("room_id").references(Room.id)
    val messageBody = varchar("message_body", 500)
    val createdDate = timestamp("created_date")

    override val primaryKey = PrimaryKey(id)
}