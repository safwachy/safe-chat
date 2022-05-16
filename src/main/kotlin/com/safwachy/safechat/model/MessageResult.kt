package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.safwachy.safechat.helper.DateTimeUtil
import java.time.LocalDateTime
import java.util.UUID

data class MessageResult(
    @JsonProperty("messageId")
    val messageId: UUID,

    @JsonProperty("message")
    var message: String,

    @JsonProperty("user")
    val user: String,

    @JsonProperty("date")
    val date: LocalDateTime,
)
