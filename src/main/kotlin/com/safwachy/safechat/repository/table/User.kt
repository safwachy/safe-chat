package com.safwachy.safechat.repository.table

import com.safwachy.safechat.model.UserModel.Companion.MAX_USER_NAME_CHAR_LENGTH
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object User : Table(){
    val id = uuid("id").uniqueIndex()
    val roomId = uuid("room_id").references(Room.id).nullable()
    val name = varchar("name", MAX_USER_NAME_CHAR_LENGTH)
    val createdDate = timestamp("created_date")

    override val primaryKey = PrimaryKey(id)
}