package com.musinsa.backend.controller

import com.musinsa.backend.dto.request.ProductManagementRequest
import com.musinsa.backend.dto.response.ApiResponse
import com.musinsa.backend.facade.BrandProductFacade
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/management")
class ProductManageController(
    private val brandProductFacade: BrandProductFacade
) {
    @PostMapping("/products")
    fun manageProduct(
        @Valid @RequestBody request: ProductManagementRequest
    ): ResponseEntity<ApiResponse> {
        return ResponseEntity.ok(brandProductFacade.handleProductManagement(request))
    }
}

