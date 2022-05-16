package com.safwachy.safechat.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.safwachy.safechat.helper.DateTimeUtil
import org.springframework.http.HttpStatus

data class RestResult(
    @JsonProperty("timestamp")
    val timestamp: String,

    @JsonProperty("status")
    val status: String,

    @JsonProperty("code")
    val code: Int,

    @JsonProperty("payload")
    val payload: Any
) {
    constructor(payload: Any): this(
        DateTimeUtil.formatCurrentDateTime(),
        "SUCCESS",
        HttpStatus.OK.value(),
        payload
    )
}
