package com.safwachy.safechat.controller

import com.safwachy.safechat.model.MessageDetail
import com.safwachy.safechat.model.RestResult
import com.safwachy.safechat.service.MessageService
import com.safwachy.safechat.service.UserService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

interface MessageController {
    fun postMessage(roomCode: String, messageDetail: MessageDetail) : ResponseEntity<RestResult>
    fun getMessage(roomCode: String, lastMessageDateTime: LocalDateTime?) : ResponseEntity<RestResult>
}

@RestController
class MessageControllerImpl (
    private val messageService: MessageService,
    private val userService: UserService
) : MessageController {
    @PostMapping("/room/{roomCode}/message")
    override fun postMessage(
        @PathVariable roomCode: String,
        @RequestBody messageDetail: MessageDetail
    ) : ResponseEntity<RestResult> {
        val user = userService.findByUserNameAndRoomCode(messageDetail.user, roomCode)
        val messageModel = messageService.postMessage(user, messageDetail.message)
        val body = RestResult(mapOf(
            "messageId" to messageModel.id,
            "message" to messageModel.messageBody,
            "user" to user.name,
            "date" to messageModel.createdDate
        ))

        return ResponseEntity.ok(body)
    }

    @GetMapping("/room/{roomCode}/message")
    override fun getMessage(
        @PathVariable roomCode: String,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") lastMessageDateTime: LocalDateTime?
    ) : ResponseEntity<RestResult>{
        TODO("Not yet implemented")

    }
}