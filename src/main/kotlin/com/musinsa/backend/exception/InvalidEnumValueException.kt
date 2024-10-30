package com.musinsa.backend.exception

class InvalidEnumValueException(
    message: String,
    errorCode: ErrorCode = ErrorCode.INVALID_INPUT
) : BusinessException(message, errorCode) {
}
