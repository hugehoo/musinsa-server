package com.musinsa.backend.dto.response

import com.musinsa.backend.common.Utils
import com.musinsa.backend.domain.Product
import com.musinsa.backend.enumerate.CategoryType
import java.math.BigDecimal


sealed class ProductVO {
    data class WithBrand(
        val category: String,
        val brand: String,
        val price: String
    ) : ProductVO() {
        companion object {
            fun from(product: Product) = WithBrand(
                category = product.category.koreanName,
                brand = product.brand.name,
                price = Utils.formatWonPrice(product.price)
            )
        }
    }

    data class WithoutBrand(
        val category: String,
        val price: String
    ) : ProductVO() {
        companion object {
            fun from(product: MinPriceByCategory) = WithoutBrand(
                category = product.category.koreanName,
                price = Utils.formatWonPrice(product.price)
            )
        }
    }

    data class MinPriceByCategory(
        val category: CategoryType,
        val price: BigDecimal
    ) : ProductVO()
}