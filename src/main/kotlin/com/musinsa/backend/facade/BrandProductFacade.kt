package com.musinsa.backend.facade

import com.musinsa.backend.config.Facade
import com.musinsa.backend.domain.Brand
import com.musinsa.backend.dto.request.BrandRequest
import com.musinsa.backend.dto.request.ProductManagementRequest
import com.musinsa.backend.dto.request.ProductRequest
import com.musinsa.backend.dto.request.RequestClassifier
import com.musinsa.backend.dto.response.ApiResponse
import com.musinsa.backend.enumerate.OperationType
import com.musinsa.backend.service.BrandService
import com.musinsa.backend.service.ProductService
import org.springframework.transaction.annotation.Transactional


@Facade
class BrandProductFacade(
    private val productService: ProductService,
    private val brandService: BrandService
) {

    @Transactional
    fun handleProductManagement(request: ProductManagementRequest): ApiResponse {
        return when (request.operationType) {
            OperationType.CREATE -> handleCreate(request)
            OperationType.UPDATE -> handleUpdate(request)
            OperationType.DELETE -> handleDelete(request)
        }
    }

    private fun handleCreate(request: ProductManagementRequest): ApiResponse {
        when {
            RequestClassifier.isBothCreateRequest(request) -> {
                createBrandAndProduct(request.brandRequest!!, request.productRequest!!)
            }
            RequestClassifier.isBrandCreateRequest(request) -> {
                createBrand(request.brandRequest!!)
            }
            RequestClassifier.isProductCreateRequest(request) -> {
                createProduct(request.productRequest!!)
            }
        }

        return ApiResponse(true, "생성이 완료되었습니다.")
    }

    private fun handleUpdate(request: ProductManagementRequest): ApiResponse {

        when {
            RequestClassifier.isBothUpdateRequest(request) -> {
                updateBrandAndProduct(request.brandRequest!!, request.productRequest!!)
            }

            RequestClassifier.isBrandUpdateRequest(request) -> {
                updateBrand(request.brandRequest!!)
            }

            RequestClassifier.isProductUpdateRequest(request) -> {
                updateProduct(request.productRequest!!)
            }
        }

        return ApiResponse(true, "update success")
    }

    private fun handleDelete(request: ProductManagementRequest): ApiResponse {
        when {
            // 브랜드와 상품 모두 삭제 요청
            RequestClassifier.isBothDeleteRequest(request) -> {
                deleteBrandWithProducts(request.brandRequest!!, request.productRequest!!)
            }
            // 브랜드만 삭제 요청
            RequestClassifier.isBrandDeleteRequest(request) -> {
                deleteBrand(request.brandRequest!!.id)
            }
            // 상품만 삭제 요청
            RequestClassifier.isProductDeleteRequest(request) -> {
                deleteProduct(request.productRequest!!)
            }
        }
        return ApiResponse(true, "삭제가 완료되었습니다.")
    }

    private fun deleteBrandWithProducts(brandRequest: BrandRequest, productRequest: ProductRequest) {
        val brand = brandService.findBrandById(productRequest.brandId)
        validateBrandMatch(brandRequest.id, brand.id!!)
        deleteBrand(brandRequest.id)
        productService.deleteAll(brand)
    }

    private fun deleteBrand(brandId: Long) {
        brandService.deleteBrand(brandId)
    }

    private fun deleteProduct(productRequest: ProductRequest) {
        val brand = brandService.findBrandById(productRequest.brandId)
        productService.deleteProduct(productRequest, brand)
    }

    private fun validateBrandMatch(requestBrandId: Long, actualBrandId: Long) {
        require(requestBrandId == actualBrandId) {
            "브랜드 ID가 일치하지 않습니다. (요청: $requestBrandId, 실제: $actualBrandId)"
        }
    }

    private fun updateBrandAndProduct(brandRequest: BrandRequest, productRequest: ProductRequest) {
        val brand = brandService.findBrandById(productRequest.brandId)
        validateBrandMatch(brandRequest.id, brand.id!!)

        // 브랜드 수정
        updateBrand(brandRequest)

        // 상품 수정
        updateProduct(productRequest)
    }

    private fun updateBrand(brandRequest: BrandRequest) {
        brandService.updateBrand(
            id = brandRequest.id,
            name = brandRequest.name
        )
    }

    private fun updateProduct(productRequest: ProductRequest) {
        productService.updateProduct(
            productId = productRequest.productId,
            request = productRequest
        )
    }

    private fun createBrandAndProduct(brandRequest: BrandRequest, productRequest: ProductRequest) {

        // 생성된 브랜드로 상품 생성
        productRequest.validateForCreate()
        val createdBrand = createBrand(brandRequest)
        productService.createProduct(
            brand = createdBrand,
            category = productRequest.category,
            price = productRequest.price
        )
    }

    private fun createBrand(brandRequest: BrandRequest): Brand {
        brandRequest.validateForCreate()
        return brandService.createBrand(brandRequest.name)
    }


    private fun createProduct(productRequest: ProductRequest) {
        productRequest.validateForCreate()
        val brand = brandService.findBrandById(productRequest.brandId)

        productService.createProduct(
            brand = brand,
            category = productRequest.category,
            price = productRequest.price
        )
    }

}