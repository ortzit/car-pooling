package org.carPooling.groupsOfPeople.application.locate

import org.carPooling.cars.domain.Car

data class LocateGroupOfPeopleResult(val id: Int, val seats: Short) {
    companion object {
        fun fromDomain(car: Car): LocateGroupOfPeopleResult =
            LocateGroupOfPeopleResult(
                id = car.id.value,
                seats = car.seats,
            )
    }
}