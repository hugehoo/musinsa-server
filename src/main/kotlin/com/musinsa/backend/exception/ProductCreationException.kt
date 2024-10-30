package com.musinsa.backend.exception

class ProductCreationException(message: String,
                               errorCode: ErrorCode = ErrorCode.DUPLICATE_ENTITY
) : BusinessException(message, errorCode) {
}
