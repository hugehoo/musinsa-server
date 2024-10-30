package com.musinsa.backend.dto.response


data class CategoryPriceResponse(
    val products: List<ProductVO>,
    val totalPrice: String
) {}

