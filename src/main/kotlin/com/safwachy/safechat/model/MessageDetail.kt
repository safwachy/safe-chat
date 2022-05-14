package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageDetail(
    @JsonProperty("user")
    val user: String,

    @JsonProperty("message")
    val message: String
)
