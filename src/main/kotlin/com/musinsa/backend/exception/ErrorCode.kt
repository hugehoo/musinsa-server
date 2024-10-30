package com.musinsa.backend.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    // 4XX 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E400", "잘못된 입력입니다"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "E404", "엔티티를 찾을 수 없습니다"),
    DUPLICATE_ENTITY(HttpStatus.CONFLICT, "E409", "이미 존재하는 엔티티입니다"),
    LAST_ENTITY_IN_CATEGORY(HttpStatus.BAD_REQUEST, "E410", "카테고리당 최소 1개의 상품은 존재해야 합니다"),

    // 5XX 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 에러가 발생했습니다");
}