package com.musinsa.backend.dto.response

import com.musinsa.backend.enumerate.CategoryType
import com.musinsa.backend.domain.Product
import com.musinsa.backend.exception.ProductCreationException
import java.math.BigDecimal

data class ProductCreateResponse(
    val id: Long,
    val brandId: Long,
    val category: CategoryType,
    val price: BigDecimal
) {
    companion object {
        fun from(product: Product): ProductCreateResponse {
            return ProductCreateResponse(
                id = product.id ?: throw ProductCreationException("상품 생성 실패: ID 없음"),
                brandId = product.brand?.id ?: throw ProductCreationException("상품 생성 실패: 브랜드 정보 없음"),
                category = product.category,
                price = product.price
            )
        }
    }
}