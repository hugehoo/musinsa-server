package com.musinsa.backend.dto.internal

import java.math.BigDecimal

data class BrandTotalPrice(
    val brandId: Long,
    val brandName: String,
    val totalPrice: BigDecimal
) {
}
