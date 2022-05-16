package com.safwachy.safechat.repository.table

import com.safwachy.safechat.model.RoomModel.Companion.ROOM_CODE_LENGTH
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Room : Table() {
    val id = uuid("id").uniqueIndex()
    val roomCode = varchar("room_code", 128)
    val createdDate = timestamp("created_date")

    override val primaryKey = PrimaryKey(id)
}