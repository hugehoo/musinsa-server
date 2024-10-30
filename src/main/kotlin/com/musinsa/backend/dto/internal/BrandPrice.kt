package com.musinsa.backend.dto.internal

import com.musinsa.backend.common.Utils
import com.musinsa.backend.dto.response.BrandPriceResponse
import java.math.BigDecimal

data class BrandPrice(
    val brand: String,
    val price: BigDecimal
) {
    fun toResponse() = BrandPriceResponse(
        brand = brand,
        price = Utils.formatWonPrice(price)
    )
}

