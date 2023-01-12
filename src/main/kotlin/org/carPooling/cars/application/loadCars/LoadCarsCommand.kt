package org.carPooling.cars.application.loadCars

import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.CarId

data class LoadCarsCommand(val cars: List<CarCommand>) {
    fun toDomain(): List<Car> = cars.map { it.toDomain() }
}

data class CarCommand(val id: Int, val seats: Short) {
    fun toDomain(): Car =
        Car(
            id = CarId(this.id),
            seats = this.seats,
            availableSeats = this.seats,
        )
}