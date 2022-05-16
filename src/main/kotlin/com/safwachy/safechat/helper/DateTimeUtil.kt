package com.safwachy.safechat.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtil {
    companion object {
        const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        fun formatCurrentDateTime() : String {
            return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now())
        }

        fun formatLocalDateTime(dateTime: LocalDateTime) : String {
            return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(dateTime)
        }
    }
}