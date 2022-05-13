package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

data class UserModel(
    @JsonProperty("id")
    val id : UUID,

    @JsonProperty("roomId")
    var roomId : UUID?,

    @JsonProperty("name")
    val name : String,

    @JsonProperty("createdDate")
    val createdDate : LocalDateTime?,
)
