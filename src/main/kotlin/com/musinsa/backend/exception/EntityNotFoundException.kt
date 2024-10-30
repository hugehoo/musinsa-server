package com.musinsa.backend.exception

class EntityNotFoundException(
    message: String,
    errorCode: ErrorCode = ErrorCode.ENTITY_NOT_FOUND
) : BusinessException(message, errorCode)