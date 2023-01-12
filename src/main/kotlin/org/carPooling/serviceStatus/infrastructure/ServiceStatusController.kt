package org.carPooling.serviceStatus.infrastructure

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServiceStatusController {
    @GetMapping("/status")
    fun status(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.OK).body("")
}