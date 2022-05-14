package com.safwachy.safechat.repository

import com.safwachy.safechat.exception.ResourceNotFoundException
import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserModel
import com.safwachy.safechat.repository.table.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.ZoneOffset
import java.util.*

interface UserRepository {
    fun createUser(user: UserModel)
    fun updateUserRoomId(userId: UUID, roomId: UUID)
    fun findByUserNameAndRoomId(userName: String, roomId: UUID) : UserModel
}

@Repository
class UserRepositoryImpl : UserRepository {
    override fun createUser(user: UserModel) {
        if (user.id != null && user.name != null && user.createdDate != null) {
            transaction {
                User.insert {
                    it[id] = user.id!!
                    it[roomId] = user.roomId
                    it[name] = user.name!!
                    it[createdDate] = user.createdDate!!.toInstant(ZoneOffset.UTC)
                }
            }
        } else {
            throw IllegalStateException("Invalid state of UserModel")
        }
    }

    override fun updateUserRoomId(userId: UUID, roomId: UUID) {
        val sql = """
            update public.user
            set room_id = '$roomId'
            where id = '$userId'
        """.trimIndent()

        transaction {
            TransactionManager.current().exec(sql)
        }
    }

    override fun findByUserNameAndRoomId(userName: String, roomId: UUID) : UserModel{
        var user = UserModel()

        val sql = """
            select * from public.user
            where name = '$userName' and room_id = '$roomId'
        """.trimIndent()

        transaction {
            TransactionManager.current().exec(sql) {
                if (!it.isBeforeFirst) return@exec
                it.next()
                user = UserModel(
                    UUID.fromString(it.getString("id")),
                    UUID.fromString(it.getString("room_id")),
                    it.getString("name"),
                    it.getTimestamp("created_date").toLocalDateTime()
                )
            }
        }

        return user
    }
}
