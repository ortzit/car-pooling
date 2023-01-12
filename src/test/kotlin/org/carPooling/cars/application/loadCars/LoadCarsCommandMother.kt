package org.carPooling.cars.application.loadCars

import org.carPooling.anyNotEmptyListOf
import org.carPooling.cars.domain.anyCardIdValue
import org.carPooling.cars.domain.anySeats

fun anyLoadCarsCommand(): LoadCarsCommand =
    LoadCarsCommand(
        anyNotEmptyListOf { anyCarCommand() }
    )

private fun anyCarCommand(): CarCommand =
    CarCommand(
        id = anyCardIdValue(),
        seats = anySeats(),
    )