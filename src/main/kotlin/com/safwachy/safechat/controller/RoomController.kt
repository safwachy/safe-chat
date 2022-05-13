package com.safwachy.safechat.controller

import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserDetail
import com.safwachy.safechat.service.RoomService
import com.safwachy.safechat.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface RoomController {
    fun join(roomCode: String?, userDetail: UserDetail) : ResponseEntity<Map<String, String>>
}

@RestController
class RoomControllerImpl (
    private val roomService: RoomService,
    private val userService: UserService
): RoomController {
    @PostMapping("/room/{roomCode}")
    override fun join(
        @PathVariable(required = false) roomCode: String?,
        @RequestBody userDetail: UserDetail
    ): ResponseEntity<Map<String, String>> {
        // create room if necessary
        val room = if (roomCode == null) roomService.create() else roomService.findByCode(roomCode)

        // create user
        val user = userService.create(userDetail.user)

        // add the user to the room
        roomService.addUser(room, user)

        // create JWT
        val jwt = "12345"

        val cookie = ResponseCookie.from("auth-token", jwt)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(86400)
            .build()

        if (room.roomCode == null) throw IllegalStateException("Room Code should not be null")

        val body = mapOf(
            "roomCode" to room.roomCode
        )

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(body)
    }
}