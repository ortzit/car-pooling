package org.carPooling.shared.infrastructure.controller

import org.carPooling.cars.infrastructure.controller.cars.CarHttpRequest
import org.carPooling.cars.infrastructure.controller.cars.CarsController
import org.carPooling.cars.infrastructure.controller.cars.anyCarHttpRequest
import org.carPooling.groupsOfPeople.domain.anyPeople
import org.carPooling.groupsOfPeople.infrastructure.controller.dropOff.DropOffController
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.JourneyController
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.JourneyHttpRequest
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.anyJourneyHttpRequest
import org.carPooling.groupsOfPeople.infrastructure.controller.locate.LocateController
import org.carPooling.groupsOfPeople.infrastructure.controller.locate.LocateResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.streams.toList

@SpringBootTest
class ControllerTest {
    @Autowired
    private lateinit var carsController: CarsController

    @Autowired
    private lateinit var journeyController: JourneyController

    @Autowired
    private lateinit var dropOffController: DropOffController

    @Autowired
    private lateinit var locateController: LocateController

    @Test
    fun `should assign cars to groups of people leaving as few free seats as possible`() {
        // given
        val fourSeatsCar = anyCarHttpRequest(4)
        val fiveSeatsCar = anyCarHttpRequest(5)
        val sixSeatsCar = anyCarHttpRequest(6)
        loadCars(
            listOf(fourSeatsCar, fiveSeatsCar, sixSeatsCar)
        )

        // when
        val fourPeopleJourneyHttpRequest = anyRegisteredJourney(4)
        val fivePeopleJourneyHttpRequest = anyRegisteredJourney(people = 5)
        val sixPeopleJourneyHttpRequest = anyRegisteredJourney(people = 6)

        val fourPeopleGroupLocateResponse = locateController.locate(fourPeopleJourneyHttpRequest.id)
        val fivePeopleGroupLocateResponse = locateController.locate(fivePeopleJourneyHttpRequest.id)
        val sixPeopleGroupLocateResponse = locateController.locate(sixPeopleJourneyHttpRequest.id)

        // then
        val expectedFourSetsCarLocateResponse = LocateResponse(id = fourSeatsCar.id, seats = fourSeatsCar.seats)
        val expectedFiveSetsCarLocateResponse = LocateResponse(id = fiveSeatsCar.id, seats = fiveSeatsCar.seats)
        val expectedSixSetsCarLocateResponse = LocateResponse(id = sixSeatsCar.id, seats = sixSeatsCar.seats)

        assertEquals(expectedFourSetsCarLocateResponse, fourPeopleGroupLocateResponse.body)
        assertEquals(expectedFiveSetsCarLocateResponse, fivePeopleGroupLocateResponse.body)
        assertEquals(expectedSixSetsCarLocateResponse, sixPeopleGroupLocateResponse.body)
    }

    @Test
    fun `should assign free seats to the groups that fit by arrival order when when enough car seats are freed`() {
        // given - A car with four seats occupied by a 4 people group
        val fourSeatsCar = anyCarHttpRequest(4)
        loadCar(fourSeatsCar)
        val fourPeopleJourneyHttpRequest = anyRegisteredJourney(people = 4)

        // when - Added 3, 2 and 1 people groups and dropped of the group that occupied the car
        val threePeopleJourneyHttpRequest = anyRegisteredJourney(people = 3)
        val twoPeopleJourneyHttpRequest = anyRegisteredJourney(people = 2)
        val onePeopleJourneyHttpRequest = anyRegisteredJourney(people = 1)

        dropOffController.dropOff(fourPeopleJourneyHttpRequest.id)

        // then - The car has 4 available seats:
        // - The first group is 3 people, enter the car (1 available seats after this step)
        // - The second group is 2 people, the car has 1 available seats, so they can not enter
        // - The third group is 1 people, enter the car (no available seats after this step)
        val threePeopleGroupLocateResponse = locateController.locate(threePeopleJourneyHttpRequest.id)
        val twoPeopleGroupLocateResponse = locateController.locate(twoPeopleJourneyHttpRequest.id)
        val onePeopleGroupLocateResponse = locateController.locate(onePeopleJourneyHttpRequest.id)

        val expectedCarLocateResponse = LocateResponse(
            id = fourSeatsCar.id,
            seats = fourSeatsCar.seats
        )
        assertEquals(expectedCarLocateResponse, threePeopleGroupLocateResponse.body)
        assertEquals(HttpStatus.NO_CONTENT, twoPeopleGroupLocateResponse.statusCode)
        assertEquals(expectedCarLocateResponse, onePeopleGroupLocateResponse.body)
    }

    @Test
    fun `should load 10^5 cars, groups of people and drop off all of them with no errors concurrently`() {
        // given
        val times = 10000
        val cars: List<CarHttpRequest> = (1..times).mapIndexed { _, idx ->
            anyCarHttpRequest().copy(id = idx)
        }
        val journeys: List<JourneyHttpRequest> = (1..times).mapIndexed { _, idx ->
            anyJourneyHttpRequest().copy(id = idx)
        }

        // when
        val carStatus = listOf(loadCars(cars).statusCode)
        val journeyResponsesStatus = journeys.parallelStream().map {
            journeyController.journey(it).statusCode
        }.toList()
        val dropOffResponsesStatus = journeys.parallelStream().map {
            dropOffController.dropOff(it.id).statusCode
        }.toList()

        val responsesStatus = listOf(carStatus, journeyResponsesStatus, dropOffResponsesStatus).flatten()
        val errors = responsesStatus.count { !it.is2xxSuccessful }

        // then
        assertEquals(0, errors)
    }

    private fun anyCarHttpRequest(seats: Short) = anyCarHttpRequest().copy(seats = seats)

    private fun loadCar(car: CarHttpRequest): ResponseEntity<*> =
        loadCars(listOf(car))

    private fun loadCars(cars: Collection<CarHttpRequest>): ResponseEntity<*> =
        carsController.loadCars(cars)

    private fun anyRegisteredJourney(people: Short = anyPeople()): JourneyHttpRequest =
        anyJourneyHttpRequest().copy(people = people)
            .apply {
                journeyController.journey(this)
            }
}