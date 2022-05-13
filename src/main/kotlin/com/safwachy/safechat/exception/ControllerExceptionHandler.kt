package com.safwachy.safechat.exception

import com.safwachy.safechat.model.ErrorDetail
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter

@ControllerAdvice
class ControllerExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [(ResourceNotFoundException::class)])
    fun handleNotFound(ex : RuntimeException, req : ServletWebRequest) : ResponseEntity<ErrorDetail> {
        val errorDetails = ErrorDetail(
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.name,
            ex.message,
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(ValidationException::class)])
    fun handleBadRequest(ex : RuntimeException, req : ServletWebRequest) : ResponseEntity<ErrorDetail> {
        val errorDetails = ErrorDetail(
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.name,
            ex.message,
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
}