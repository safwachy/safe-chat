package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

data class UserModel(
    @JsonProperty("id")
    var id : UUID?,

    @JsonProperty("roomId")
    var roomId : UUID?,

    @JsonProperty("name")
    var name : String?,

    @JsonProperty("createdDate")
    var createdDate : LocalDateTime?,
) {
    constructor() : this(null, null, null, null)

    companion object {
        const val MAX_USER_NAME_LENGTH = 50
    }
}
