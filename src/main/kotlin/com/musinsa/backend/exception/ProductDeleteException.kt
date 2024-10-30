package com.musinsa.backend.exception

class ProductDeleteException(message: String,
                             errorCode: ErrorCode = ErrorCode.LAST_ENTITY_IN_CATEGORY
) : BusinessException(message, errorCode) {
}
