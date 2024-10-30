package com.musinsa.backend.dto.response

import com.musinsa.backend.common.Utils
import com.musinsa.backend.dto.internal.BrandTotalPrice


data class BrandTotalSummary(
    val brand: String,
    val products: List<ProductVO>,
    val totalPrice: String
) {
    companion object {
        fun of(products: List<ProductVO.MinPriceByCategory>, brandTotalPrice: BrandTotalPrice) =
            BrandTotalSummary(
                brandTotalPrice.brandName,
                products.map { ProductVO.WithoutBrand.from(it) },
                Utils.formatWonPrice(brandTotalPrice.totalPrice)
            )
    }
}