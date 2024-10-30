package com.musinsa.backend.repository

import com.musinsa.backend.domain.Brand
import com.musinsa.backend.enumerate.CategoryType
import com.musinsa.backend.domain.Product
import com.musinsa.backend.dto.internal.BrandTotalPrice
import com.musinsa.backend.dto.internal.BrandPrice
import com.musinsa.backend.dto.response.ProductVO
import com.musinsa.backend.enumerate.PriceType

interface CustomProductRepository {
    fun findLowestPriceProductsByCategory(): Map<CategoryType, Product>
    fun findMinMaxPriceBrand(categoryType: CategoryType, priceType: PriceType): List<BrandPrice>?
    fun deleteAllProducts(brand: Brand)
    fun getMinimumTotalPriceBrand(): BrandTotalPrice?
    fun getAllProductsByBrand(brandId: Long): List<ProductVO.MinPriceByCategory>

}