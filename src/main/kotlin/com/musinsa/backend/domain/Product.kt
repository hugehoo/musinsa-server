package com.musinsa.backend.domain

import com.musinsa.backend.enumerate.CategoryType
import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(
    name = "products",
//    uniqueConstraints = [
//        UniqueConstraint(
//            name = "uk_brand_category",
//            columnNames = ["brand_id", "category"]
//        )
//    ]
)
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    var brand: Brand,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: CategoryType,

    @Column(nullable = false)
    var price: BigDecimal,

    @Column(nullable = false, columnDefinition = "boolean default false")
    var deleted: Boolean = false,

    ) {
    fun updatePrice(newPrice: BigDecimal) {
        require(newPrice >= BigDecimal.ZERO) { "가격은 0 이상이어야 합니다." }
        this.price = newPrice
    }

    fun remove() {
        this.deleted = true
    }

    init {
        require(price >= BigDecimal.ZERO) { "가격은 0 이상이어야 합니다." }
    }
}