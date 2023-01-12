package org.carPooling.shared.infrastructure

import org.springframework.http.ResponseEntity

interface ResponseEntityMapper {
    fun initialize(response: Any?): Boolean
    fun toResponseEntity(): ResponseEntity<*>
}