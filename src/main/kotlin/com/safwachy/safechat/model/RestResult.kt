package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

data class RestResult(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("code")
    val code: Int,

    @JsonProperty("payload")
    val payload: Any
) {
    constructor(payload: Any): this("SUCCESS", HttpStatus.OK.value(), payload)
}
