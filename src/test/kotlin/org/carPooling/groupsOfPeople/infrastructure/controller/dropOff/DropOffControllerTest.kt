package org.carPooling.groupsOfPeople.infrastructure.controller.dropOff

import org.carPooling.cars.infrastructure.controller.cars.CarsController
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.JourneyController
import org.carPooling.shared.infrastructure.controller.anyLoadedCarHttpRequest
import org.carPooling.shared.infrastructure.controller.anyMatchingCarRegisteredJourneyGroupId
import org.carPooling.shared.infrastructure.controller.anyRegisteredJourneyGroupId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class DropOffControllerTest {
    @Autowired
    private lateinit var controller: DropOffController

    @Autowired
    private lateinit var journeyController: JourneyController

    @Autowired
    private lateinit var carsController: CarsController

    @Test
    fun `should return ok or no content when dropping off an existing group with no car assigned`() {
        // given
        val registeredJourneyGroupId = anyRegisteredJourneyGroupId(journeyController)

        // when
        val result = controller.dropOff(registeredJourneyGroupId)

        // then
        assertTrue(result.statusCode == HttpStatus.OK || result.statusCode == HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should return ok or no content when dropping off an existing group with car assigned`() {
        // given
        val loadedCarHttpRequest = anyLoadedCarHttpRequest(carsController)
        val registeredJourneyGroupId = anyMatchingCarRegisteredJourneyGroupId(
            journeyController = journeyController,
            carHttpRequest = loadedCarHttpRequest,
        )

        // when
        val result = controller.dropOff(registeredJourneyGroupId)

        // then
        assertTrue(result.statusCode == HttpStatus.OK || result.statusCode == HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should return not found when dropping off a non existing group with no car assigned`() {
        // given
        val notRegisteredGroupOfPeopleId = anyGroupOfPeopleIdValue()

        // when
        val result = controller.dropOff(notRegisteredGroupOfPeopleId)

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}