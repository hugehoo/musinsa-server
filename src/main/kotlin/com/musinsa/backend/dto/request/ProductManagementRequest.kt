package com.musinsa.backend.dto.request

import com.musinsa.backend.enumerate.OperationType
import jakarta.validation.Valid

data class ProductManagementRequest(
    val operationType: OperationType,
    val brandRequest: BrandRequest?,
    val productRequest: ProductRequest?
)


