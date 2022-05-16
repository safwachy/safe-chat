package com.safwachy.safechat.exception

import com.safwachy.safechat.helper.DateTimeUtil
import com.safwachy.safechat.model.ErrorDetail
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.logging.Logger

@ControllerAdvice
class ControllerExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [(ResourceNotFoundException::class)])
    fun handleNotFound(ex : RuntimeException, req : ServletWebRequest) : ResponseEntity<ErrorDetail> {
        LOG.info("404 Exception: ${ex.printStackTrace()}")
        val errorDetails = ErrorDetail(
            DateTimeUtil.formatCurrentDateTime(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.name,
            ex.message,
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(ValidationException::class)])
    fun handleBadRequest(ex : RuntimeException, req : ServletWebRequest) : ResponseEntity<ErrorDetail> {
        LOG.info("400 Exception: ${ex.printStackTrace()}")
        val errorDetails = ErrorDetail(
            DateTimeUtil.formatCurrentDateTime(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name,
            ex.message,
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleInternalServerError(ex : RuntimeException, req : ServletWebRequest) : ResponseEntity<ErrorDetail> {
        LOG.info("500 Exception: ${ex.printStackTrace()}")
        val errorDetails = ErrorDetail(
            DateTimeUtil.formatCurrentDateTime(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            "Something went wrong",
        )
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        val LOG: Logger = Logger.getLogger(ControllerExceptionHandler::class.java.name)
    }
}