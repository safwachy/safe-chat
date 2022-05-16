package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageDetail(
    @JsonProperty("sender")
    val sender: String,

    @JsonProperty("roomCode")
    val roomCode: String,

    @JsonProperty("message")
    val message: String
)
