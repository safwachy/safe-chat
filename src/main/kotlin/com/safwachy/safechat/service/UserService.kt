package com.safwachy.safechat.service

import com.safwachy.safechat.model.UserModel
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface UserService {
    fun create(userName: String) : UserModel
}

@Service
class UserServiceImpl : UserService {
    override fun create(userName: String) : UserModel {
        val user = UserModel(UUID.randomUUID(), null, userName, LocalDateTime.now())
        return user
    }
}