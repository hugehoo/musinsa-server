package com.musinsa.backend.repository

import com.musinsa.backend.domain.Brand
import com.musinsa.backend.enumerate.CategoryType
import com.musinsa.backend.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<Product, Long>, CustomProductRepository {

    fun countByBrandAndCategoryAndDeleted(brand: Brand, categoryType: CategoryType, deleted: Boolean) : Long
}