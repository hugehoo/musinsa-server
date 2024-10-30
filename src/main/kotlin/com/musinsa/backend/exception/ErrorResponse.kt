package com.musinsa.backend.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val code: String,
    val message: String,
    val path: String,
) {

    companion object {
        fun of(errorCode: ErrorCode, path: String) = ErrorResponse(
            status = errorCode.status.value(),
            code = errorCode.code,
            message = errorCode.message,
            path = path
        )

        fun of(ex: BusinessException, path: String) = ErrorResponse(
            status = ex.errorCode.status.value(),
            code = ex.errorCode.code,
            message = ex.message,
            path = path
        )
    }
}