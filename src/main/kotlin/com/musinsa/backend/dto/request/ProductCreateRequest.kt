package com.musinsa.backend.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProductCreateRequest(
    @field:NotBlank(message = "상품명은 필수입니다.")
    val name: String,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    val category: String,

    @field:NotNull(message = "가격은 필수입니다.")
    @field:Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    val price: BigDecimal
) {
}
