package com.safwachy.safechat.controller

import com.safwachy.safechat.exception.ResourceNotFoundException
import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.helper.DateTimeUtil
import com.safwachy.safechat.model.MessageDetail
import com.safwachy.safechat.model.MessageResult
import com.safwachy.safechat.model.RestResult
import com.safwachy.safechat.service.MessageService
import com.safwachy.safechat.service.RoomService
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
    fun postMessage(messageDetail: MessageDetail) : ResponseEntity<RestResult>
    fun getMessage(roomCode: String, lastMessageDateTime: LocalDateTime?) : ResponseEntity<RestResult>
}

@RestController
class MessageControllerImpl (
    private val messageService: MessageService,
    private val userService: UserService,
    private val roomService: RoomService
) : MessageController {
    @PostMapping("/message")
    override fun postMessage(
        @RequestBody messageDetail: MessageDetail
    ) : ResponseEntity<RestResult> {
        val user = userService.findByUserNameAndRoomCode(messageDetail.sender, messageDetail.roomCode)
        if (user.id == null) throw ResourceNotFoundException("User does not exist in room")
        val messageModel = messageService.postMessage(user, messageDetail.message)
        val body = RestResult(MessageResult(messageModel.id, messageModel.messageBody, user.name!!, messageModel.createdDate))
        return ResponseEntity.ok(body)
    }

    @GetMapping("/room/{roomCode}/message")
    override fun getMessage(
        @PathVariable roomCode: String,
        @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT) lastMessageDateTime: LocalDateTime?
    ) : ResponseEntity<RestResult>{
        val room = roomService.findByCode(roomCode)
        val body = RestResult(messageService.getMessage(room, lastMessageDateTime))
        return ResponseEntity.ok(body)
    }
}