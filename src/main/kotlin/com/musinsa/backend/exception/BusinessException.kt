package com.musinsa.backend.exception

sealed class BusinessException(
    override val message: String,
    val errorCode: ErrorCode
) : RuntimeException(message)
