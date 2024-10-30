package com.musinsa.backend.dto.request

import com.musinsa.backend.enumerate.CategoryType
import java.math.BigDecimal

data class ProductRequest(
    val brandId: Long,
    val productId: Long,
    val category: CategoryType,
    val price: BigDecimal,
    val deleted: Boolean
) {
    fun validateForCreate() {
        require(price >= BigDecimal.ZERO) { "가격은 0 이상이어야 합니다." }
    }
}
