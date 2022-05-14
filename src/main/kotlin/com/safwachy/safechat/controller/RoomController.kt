package com.safwachy.safechat.controller

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
    @PostMapping(value = ["/room/{roomCode}", "/room", "/room/"])
    override fun join(
        @PathVariable(required = false) roomCode: String?,
        @RequestBody userDetail: UserDetail
    ): ResponseEntity<Map<String, String>> {
        // create user
        val user = userService.create(userDetail.user, roomCode)

        // create room if necessary
        val room = if (roomCode == null) roomService.create() else roomService.findByCode(roomCode)
        if (room.roomCode == null) throw IllegalArgumentException("Room Code should not be null")

        // add the user to the room
        userService.updateRoom(user, room)

        // create JWT
        val jwt = "12345"

        val cookie = ResponseCookie.from("auth-token", jwt)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(86400)
            .build()

        val body = mapOf(
            "roomCode" to room.roomCode
        )

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(body)
    }
}