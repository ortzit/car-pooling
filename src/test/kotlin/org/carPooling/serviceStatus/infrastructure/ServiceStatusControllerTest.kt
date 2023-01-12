package org.carPooling.serviceStatus.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ServiceStatusControllerTest {
    private val controller: ServiceStatusController = ServiceStatusController()

    @Test
    fun `should return ok response`() {
        // when
        val response = controller.status()

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
    }
}