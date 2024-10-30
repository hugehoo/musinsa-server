package com.musinsa.backend.service

import com.musinsa.backend.common.Utils
import com.musinsa.backend.domain.Brand
import com.musinsa.backend.enumerate.CategoryType
import com.musinsa.backend.domain.Product
import com.musinsa.backend.dto.response.ProductVO
import com.musinsa.backend.dto.response.CategoryPriceResponse
import com.musinsa.backend.dto.request.ProductRequest
import com.musinsa.backend.dto.response.BrandTotalSummary
import com.musinsa.backend.dto.response.CategoryPriceRangeResponse
import com.musinsa.backend.enumerate.PriceType
import com.musinsa.backend.exception.EntityNotFoundException
import com.musinsa.backend.exception.ProductDeleteException
import com.musinsa.backend.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal


@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    /**
     * 1번 API
     */
    @Transactional(readOnly = true)
    fun findLowestPriceByCategories(): CategoryPriceResponse {
        val categoryLowestPriceMap: Map<CategoryType, Product> = productRepository.findLowestPriceProductsByCategory()

        val lowestPriceByCategory = categoryLowestPriceMap.map { product ->
            ProductVO.WithBrand.from(product = product.value)
        }

        val totalLowestPrice = categoryLowestPriceMap.map { it.value.price }
            .reduce { acc, price -> acc + price }

        return CategoryPriceResponse(
            products = lowestPriceByCategory,
            totalPrice = Utils.formatWonPrice(totalLowestPrice)
        )
    }

    /**
     * 2번 api
     * 최저가격이 겹치는 상황이면? -> 1. 예외 던진다. 2. 리스트로 묶어서 보낸다.
     * API 자체가 잘못된 게 아닌데, 단지 최저가가 겹치는 이유로 예외를 던지는건 이상한 대처 같다.
     */
    @Transactional(readOnly = true)
    fun findLowestPriceBrand(): BrandTotalSummary? {
        val minTotalPriceBrand = productRepository.getMinimumTotalPriceBrand()
        val products = productRepository.getAllProductsByBrand(minTotalPriceBrand!!.brandId)
        return BrandTotalSummary.of(products, minTotalPriceBrand)
    }


    /**
     * 3번 api
     */
    @Transactional(readOnly = true)
    fun findPriceRangeByCategory(categoryName: String): CategoryPriceRangeResponse? {

        val minimumProduct = productRepository.findMinMaxPriceBrand(CategoryType.fromValue(categoryName), PriceType.MIN)!!
        val maximumProduct = productRepository.findMinMaxPriceBrand(CategoryType.fromValue(categoryName), PriceType.MAX)!!
        return CategoryPriceRangeResponse(
            categoryName,
            minimumProduct.map { it.toResponse() },
            maximumProduct.map { it.toResponse() }
        )
    }

    fun createProduct(
        brand: Brand,
        category: CategoryType,
        price: BigDecimal
    ): Product {
        val product = Product(
            brand = brand,
            category = category,
            price = price
        )

        return productRepository.save(product)
    }


    /**
     * update 는 가격과 삭제만 변경
     */
    @Transactional
    fun updateProduct(productId: Long, request: ProductRequest) {
        val product = getProduct(productId)
        product.updatePrice(request.price)
        // 이거 생각많이 해얗나는게
        // 삭제과정 + 카테고리 변경까지 있으면 어케됨.  -> 카테고리 변경은 못하게 막자. 애초에 카테고리 변경할바엔 삭제하고 새로운 상품 등록 하는걸로.
    }

    @Transactional
    fun deleteProduct(request: ProductRequest, brand: Brand) {
        val existProducts: Long =
            productRepository.countByBrandAndCategoryAndDeleted(brand, request.category, false)

        if (existProducts > 1) {
            val product = getProduct(request.productId)
            product.remove()
        } else {
            throw ProductDeleteException("category 당 상품은 최소 1개는 존재해야 한다.")
        }
    }

    private fun getProduct(productId: Long): Product {
        return productRepository.findById(productId)
            .orElseThrow { EntityNotFoundException("[ID]:$productId 상품을 찾을 수 없습니다.") }
    }

    fun deleteAll(brand: Brand) {
        productRepository.deleteAllProducts(brand)
    }

}