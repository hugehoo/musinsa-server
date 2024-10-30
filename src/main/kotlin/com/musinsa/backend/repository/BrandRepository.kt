package com.musinsa.backend.repository

import com.musinsa.backend.domain.Brand
import com.musinsa.backend.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByName(name: String): Boolean
}
