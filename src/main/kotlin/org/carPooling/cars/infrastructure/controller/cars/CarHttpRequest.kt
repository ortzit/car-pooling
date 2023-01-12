package org.carPooling.cars.infrastructure.controller.cars

import kotlinx.serialization.Serializable
import org.carPooling.cars.application.loadCars.CarCommand
import org.carPooling.cars.application.loadCars.LoadCarsCommand

@Serializable
data class CarHttpRequest(val id: Int, val seats: Short) {
    fun toCommand(): CarCommand =
        CarCommand(
            id = id,
            seats = seats,
        )
}

fun Collection<CarHttpRequest>.toCommand(): LoadCarsCommand =
    LoadCarsCommand(
        this.map { it.toCommand() }
    )