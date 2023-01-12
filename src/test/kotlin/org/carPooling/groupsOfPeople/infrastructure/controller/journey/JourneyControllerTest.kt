package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.anyPositiveShort
import org.carPooling.cars.infrastructure.controller.cars.CarHttpRequest
import org.carPooling.cars.infrastructure.controller.cars.CarsController
import org.carPooling.shared.infrastructure.controller.anyLoadedCarHttpRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class JourneyControllerTest {
    @Autowired
    private lateinit var journeyController: JourneyController

    @Autowired
    private lateinit var carsController: CarsController

    @Test
    fun `should return ok or accepted response when adding group of people with no cars to assign`() {
        // given
        val journeyHttpRequest = anyJourneyHttpRequest()

        // when
        val result = journeyController.journey(journeyHttpRequest)

        // then
        assertTrue(result.statusCode == HttpStatus.OK || result.statusCode == HttpStatus.ACCEPTED)
    }

    @Test
    fun `should return ok or accepted response when adding group of people with car to assign`() {
        // given
        val carHttpRequest = anyLoadedCarHttpRequest(carsController)
        val journeyHttpRequestMatchingCar = anyJourneyHttpRequestMatchingCar(carHttpRequest)

        // when
        val result = journeyController.journey(journeyHttpRequestMatchingCar)

        // then
        assertTrue(result.statusCode == HttpStatus.OK || result.statusCode == HttpStatus.ACCEPTED)
    }


    @Test
    fun `should return bad request response when adding group of people with invalid amount of people`() {
        // given
        val journeyHttpRequest = anyJourneyHttpRequestWithInvalidPeople()

        // when
        val result = journeyController.journey(journeyHttpRequest)

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    private fun anyJourneyHttpRequestWithInvalidPeople(): JourneyHttpRequest =
        anyPositiveShort(minValue = 7, maxValue = Short.MAX_VALUE).let { invalidPeople ->
            anyJourneyHttpRequest().copy(people = invalidPeople)
        }

    private fun anyJourneyHttpRequestMatchingCar(carHttpRequest: CarHttpRequest): JourneyHttpRequest =
        anyJourneyHttpRequest().copy(people = carHttpRequest.seats)
}