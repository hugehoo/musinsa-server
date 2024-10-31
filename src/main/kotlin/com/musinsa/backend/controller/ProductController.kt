package com.musinsa.backend.controller

import com.musinsa.backend.dto.response.CategoryPriceResponse
import com.musinsa.backend.dto.response.BrandTotalSummary
import com.musinsa.backend.dto.response.CategoryPriceRangeResponse
import com.musinsa.backend.dto.response.LowestPriceResponse
import com.musinsa.backend.service.ProductService
import org.springframework.web.bind.annotation.RestController

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class ProductController(
    private val productService: ProductService,
) {
    /**
     * 1. 카테고리별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     */
    @GetMapping("/category/lowest-price")
    fun getLowestPriceByCategories(): ResponseEntity<CategoryPriceResponse> {
        return ResponseEntity.ok(productService.findLowestPriceByCategories())
    }

    /**
     * 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격 브랜드와 총액을 조회하는 API
     */
    @GetMapping("/brand/lowest-price")
    fun getLowestPriceBrand(): ResponseEntity<LowestPriceResponse> {
        val findLowestPriceBrand : BrandTotalSummary? = productService.findLowestPriceBrand()
        return ResponseEntity.ok(LowestPriceResponse(findLowestPriceBrand!!))
    }

    /**
     * 3. 카테고리 이름으로 최저가, 최고가 브랜드와 상품 가격을 조회하는 API
     */
    @GetMapping("/price-range")
    fun getPriceRangeByCategory(
        @RequestParam categoryName: String
    ): ResponseEntity<CategoryPriceRangeResponse> {
        return ResponseEntity.ok(productService.findPriceRangeByCategory(categoryName))
    }


}


