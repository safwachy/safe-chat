package com.safwachy.safechat.model


data class ErrorDetail (
    var timestamp: String,
    var status: Int,
    var error: String,
    var message: String?,
)