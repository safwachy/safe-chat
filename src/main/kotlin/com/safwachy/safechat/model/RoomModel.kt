package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

data class RoomModel(
    @JsonProperty("id")
    val id : UUID?,

    @JsonProperty("roomCode")
    var roomCode : String?,

    @JsonProperty("createdDate")
    val createdDate : LocalDateTime?,
) {
    constructor() : this(null, null, null)

    companion object {
        const val ROOM_CODE_LENGTH = 20
    }
}
