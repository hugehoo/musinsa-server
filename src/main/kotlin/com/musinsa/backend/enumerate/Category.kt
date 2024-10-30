package com.musinsa.backend.enumerate

import com.musinsa.backend.exception.InvalidEnumValueException

enum class CategoryType(
    val koreanName: String
) {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("바지"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    CAP("모자"),
    SOCKS("양말"),
    BRA("브라"),
    ACCESSORY("액세서리");


    companion object {
        fun fromValue(value: String): CategoryType {
            return try {
                CategoryType.valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidEnumValueException("해당 입력값($value)은 유효하지 않은 카테고리 값입니다.")
            }
        }
    }
}