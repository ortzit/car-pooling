package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import org.carPooling.cars.infrastructure.controller.cars.CarHttpRequest
import org.carPooling.cars.infrastructure.controller.cars.CarsController
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.JourneyController
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.anyJourneyHttpRequest
import org.carPooling.shared.infrastructure.controller.anyLoadedCarHttpRequest
import org.carPooling.shared.infrastructure.controller.anyRegisteredJourneyGroupId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class LocateControllerTest {
    @Autowired
    private lateinit var controller: LocateController

    @Autowired
    private lateinit var journeyController: JourneyController

    @Autowired
    private lateinit var carsController: CarsController

    @Test
    fun `should return no content when locating a group with no assigned car`() {
        // given
        val registeredGroupId = anyRegisteredJourneyGroupId(journeyController)

        // when
        val result = controller.locate(registeredGroupId)

        // then
        assertEquals(HttpStatus.NO_CONTENT, result.statusCode)
    }

    @Test
    fun `should return ok with assigned car when locating a group with assigned car`() {
        // given
        val loadedCarHttpRequest = anyLoadedCarHttpRequest(carsController)
        val matchingCarRegisteredGroupId = anyMatchingCarRegisteredJourneyGroupId(loadedCarHttpRequest)

        // when
        val result = controller.locate(matchingCarRegisteredGroupId)

        // then
        assertEquals(HttpStatus.OK, result.statusCode)
        val expectedLocateResponse = LocateResponse(
            id = loadedCarHttpRequest.id,
            seats = loadedCarHttpRequest.seats
        )
        assertEquals(expectedLocateResponse, result.body)
    }

    @Test
    fun `should return not found when locating a non existing group`() {
        // given
        val notRegisteredGroupOfPeopleId = anyGroupOfPeopleIdValue()

        // when
        val result = controller.locate(notRegisteredGroupOfPeopleId)

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    private fun anyMatchingCarRegisteredJourneyGroupId(carHttpRequest: CarHttpRequest): Int =
        anyJourneyHttpRequest().copy(people = carHttpRequest.seats)
            .apply {
                journeyController.journey(this)
            }.id
}