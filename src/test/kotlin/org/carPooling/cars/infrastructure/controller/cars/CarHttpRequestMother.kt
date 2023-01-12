package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.cars.domain.anyCardIdValue
import org.carPooling.cars.domain.anySeats

fun anyCarHttpRequest(): CarHttpRequest =
    CarHttpRequest(
        id = anyCardIdValue(),
        seats = anySeats()
    )