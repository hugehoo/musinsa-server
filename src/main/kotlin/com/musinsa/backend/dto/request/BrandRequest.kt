package com.musinsa.backend.dto.request

data class BrandRequest(
    val id: Long,
    val name: String
) {
    fun validateForCreate() {
        require(name.isNotBlank()) { "브랜드 이름은 빈 값일 수 없습니다." }
    }
}
