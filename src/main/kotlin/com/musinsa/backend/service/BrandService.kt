package com.musinsa.backend.service

import com.musinsa.backend.domain.Brand
import com.musinsa.backend.exception.EntityNotFoundException
import com.musinsa.backend.exception.ProductCreationException
import com.musinsa.backend.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    @Transactional(readOnly = true)
    fun findBrandById(brandId: Long): Brand {
        return getBrand(brandId)
    }

    @Transactional
    fun createBrand(brandName: String): Brand {

        // 이름 중복 체크
        if (brandRepository.existsByName(brandName)) {
            throw ProductCreationException("이미 존재하는 브랜드명입니다: $brandName")
        }
        val newBrand = Brand(name = brandName)
        return brandRepository.save(newBrand)
    }

    @Transactional
    fun updateBrand(id: Long, name: String) {
        val brand = getBrand(id)
        brand.changeName(name)
    }

    @Transactional
    fun deleteBrand(id: Long) {
        val brand = getBrand(id)
        brand.removeBrand()
    }

    private fun getBrand(id: Long): Brand {
        return brandRepository.findById(id)
            .orElseThrow { EntityNotFoundException("[ID]: $id 브랜드를 찾을 수 없습니다.") }
    }
}
