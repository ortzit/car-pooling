package org.carPooling.shared.infrastructure.controller

import org.carPooling.cars.infrastructure.controller.cars.CarHttpRequest
import org.carPooling.cars.infrastructure.controller.cars.CarsController
import org.carPooling.cars.infrastructure.controller.cars.anyCarHttpRequest
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.JourneyController
import org.carPooling.groupsOfPeople.infrastructure.controller.journey.anyJourneyHttpRequest

fun anyRegisteredJourneyGroupId(journeyController: JourneyController): Int =
    anyJourneyHttpRequest()
        .apply {
            journeyController.journey(this)
        }.id

fun anyLoadedCarHttpRequest(carsController: CarsController): CarHttpRequest =
    anyCarHttpRequest().apply {
        carsController.loadCars(listOf(this))
    }

fun anyMatchingCarRegisteredJourneyGroupId(
    journeyController: JourneyController,
    carHttpRequest: CarHttpRequest
): Int =
    anyJourneyHttpRequest().copy(people = carHttpRequest.seats)
        .apply {
            journeyController.journey(this)
        }.id