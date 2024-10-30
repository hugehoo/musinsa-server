package com.musinsa.backend.dto.request

import com.musinsa.backend.dto.request.ProductManagementRequest

class RequestClassifier {
    companion object{
        fun isBothDeleteRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest != null

        fun isBrandDeleteRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest == null

        fun isProductDeleteRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest == null && request.productRequest != null

        fun isBothUpdateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest != null

        fun isBrandUpdateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest == null

        fun isProductUpdateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest == null && request.productRequest != null


        fun isBothCreateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest != null

        fun isBrandCreateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest != null && request.productRequest == null

        fun isProductCreateRequest(request: ProductManagementRequest): Boolean =
            request.brandRequest == null && request.productRequest != null
    }
}