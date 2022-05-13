package com.safwachy.safechat.repository.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

object User : Table(){
    val id = uuid("id")
    val roomId = uuid("room_id").references(Room.id)
    val name = varchar("name", 50)
    val createdDate = timestamp("created_date")

    override val primaryKey = PrimaryKey(id)
}