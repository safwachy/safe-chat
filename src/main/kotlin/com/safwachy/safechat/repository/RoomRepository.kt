package com.safwachy.safechat.repository

import com.safwachy.safechat.exception.ResourceNotFoundException
import com.safwachy.safechat.model.RoomModel
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import com.safwachy.safechat.repository.table.Room
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

interface RoomRepository {
    fun insertRoom(room: RoomModel)
    fun findByCode(roomCode: String) : RoomModel
}

@Repository
class RoomRepositoryImpl : RoomRepository {
    override fun insertRoom(room: RoomModel) {
        if (room.id != null && room.roomCode != null && room.createdDate != null) {
            transaction {
                Room.insert {
                    it[id] = room.id
                    it[roomCode] = room.roomCode
                    it[createdDate] = room.createdDate.toInstant(ZoneOffset.UTC)
                }
            }
        } else {
            throw IllegalArgumentException("Invalid state of RoomModel")
        }
    }

    override fun findByCode(roomCode: String): RoomModel {
        var room = RoomModel()

        val sql = """
            select * from room
            where room_code = '$roomCode'
        """.trimIndent()

        transaction {
            TransactionManager.current().exec(sql) {
                if (!it.isBeforeFirst) throw ResourceNotFoundException("Could not find a room with code: $roomCode")
                it.next()
                room = RoomModel(
                    UUID.fromString(it.getString("id")),
                    it.getString("room_code"),
                    it.getTimestamp("created_date").toLocalDateTime()
                )
            }
        }

        return room
    }
}

