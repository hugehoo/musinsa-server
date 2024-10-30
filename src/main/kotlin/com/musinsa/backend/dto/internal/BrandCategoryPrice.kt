package com.musinsa.backend.dto.internal

import com.musinsa.backend.enumerate.CategoryType
import java.math.BigDecimal

data class BrandCategoryPrice(
    val brandId: Long,
    val category: CategoryType,
    val totalPrice: BigDecimal
) {
}
