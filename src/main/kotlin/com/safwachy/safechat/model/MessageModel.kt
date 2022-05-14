package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID

data class MessageModel(
    @JsonProperty("id")
    val id: UUID,

    @JsonProperty("creatorId")
    val creatorId: UUID,

    @JsonProperty("roomId")
    val roomId: UUID,

    @JsonProperty("messageBody")
    val messageBody: String,

    @JsonProperty("createdDate")
    val createdDate: LocalDateTime,

) {
    companion object {
        const val MAX_MESSAGE_CHAR_LENGTH = 500
    }
}
