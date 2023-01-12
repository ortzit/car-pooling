package org.carPooling.shared.infrastructure

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

abstract class ResponseEntityFactory(
    private val responseEntityMappers: Collection<ResponseEntityMapper> = emptyList()
) {
    fun build(response: Any?): ResponseEntity<*> =
        responseEntityMappers.firstOrNull { responseEntityMapper ->
            responseEntityMapper.initialize(response)
        }?.toResponseEntity()
            ?: notImplementedResponseEntity()

    private fun notImplementedResponseEntity(): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body("")
}