package com.safwachy.safechat.controller

import com.safwachy.safechat.helper.SecurityUtil
import com.safwachy.safechat.model.RestResult
import com.safwachy.safechat.model.RoomModel
import com.safwachy.safechat.model.UserDetail
import com.safwachy.safechat.service.RoomService
import com.safwachy.safechat.service.UserService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface RoomController {
    fun join(roomCode: String?, userDetail: UserDetail) : ResponseEntity<RestResult>
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
    ): ResponseEntity<RestResult> {
        // create user
        val user = userService.create(userDetail.user, roomCode)

        // create room if necessary
        val code: String
        val room: RoomModel
        if (roomCode == null) {
            code = RandomStringUtils.randomAlphanumeric(RoomModel.ROOM_CODE_LENGTH)
            room = roomService.create(code)
        } else {
            code = roomCode
            room = roomService.findByCode(roomCode)
        }
        if (room.roomCode == null) throw IllegalArgumentException("Room Code should not be null")

        // add the user to the room
        userService.updateRoom(user, room)

        val body = RestResult(mapOf(
            "roomCode" to code
        ))

        return ResponseEntity.ok(body)
    }
}