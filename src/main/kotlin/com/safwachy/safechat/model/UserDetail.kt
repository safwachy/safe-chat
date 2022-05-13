package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetail(
    @JsonProperty("user")
    val user : String
)
