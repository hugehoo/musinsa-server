package com.musinsa.backend.domain

import jakarta.persistence.*

@Entity
@Table(name = "brands")
class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @OneToMany(mappedBy = "brand", cascade = [CascadeType.ALL], orphanRemoval = true)
    var products: MutableList<Product> = mutableListOf(),

    @Column(nullable = false, columnDefinition = "boolean default false")
    var isDel: Boolean = false,
    ) {
    fun addProduct(product: Product) {
        products.add(product)
        product.brand = this
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun removeProduct() {
        isDel = true
    }

    fun removeBrand() {
        isDel = true
    }
}