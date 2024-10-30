package com.musinsa.backend.dto.response

data class CategoryPriceRangeResponse(
    val category: String,
    val lowestPrice: List<BrandPriceResponse>,
    val highestPrice: List<BrandPriceResponse>
)