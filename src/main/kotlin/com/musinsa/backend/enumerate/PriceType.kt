package com.musinsa.backend.enumerate

import com.musinsa.backend.exception.InvalidEnumValueException

enum class PriceType {
    MIN, MAX;

    companion object {
        fun fromValue(value: String): PriceType {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidEnumValueException("유효하지 않은 가격 타입입니다. 입력값: $value (가능한 값: MIN, MAX)")
            }
        }
    }
}
