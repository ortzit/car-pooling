package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.anyNotEmptyListOf
import org.carPooling.anyPositiveShort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class CarsControllerTest {
    @Autowired
    private lateinit var carsController: CarsController

    @Test
    fun `should return ok response when adding valid cars`() {
        // given
        val carsRequest = anyNotEmptyListOf { anyCarHttpRequest() }

        // when
        val result = carsController.loadCars(carsRequest)

        // then
        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun `should return bad request response when adding a car with invalid seats`() {
        // given
        val carRequest = anyCarHttpRequestWithInvalidSeats()

        // when
        val result = carsController.loadCars(listOf(carRequest))

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun `should return bad request response when adding 2 cars with the same id`() {
        // given
        val carsRequest = anyCarsHttpRequestWithDuplicatedIds()

        // when
        val result = carsController.loadCars(carsRequest)

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    private fun anyCarHttpRequestWithInvalidSeats(): CarHttpRequest =
        anyPositiveShort(minValue = 1, maxValue = 3)
            .let { invalidSeats ->
                anyCarHttpRequest().copy(seats = invalidSeats)
            }

    private fun anyCarsHttpRequestWithDuplicatedIds(): List<CarHttpRequest> =
        anyCarHttpRequest().let { carHttpRequest ->
            listOf(
                carHttpRequest,
                carHttpRequest.copy()
            )
        }
}