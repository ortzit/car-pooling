package org.carPooling.cars.infrastructure.repository

import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.CarId

data class CarEntity(val id: Int, val seats: Short, var availableSeats: Short) {
    fun toDomain(): Car =
        Car(
            id = CarId(this.id),
            seats = this.seats,
            availableSeats = this.availableSeats,
        )

    fun decreaseAvailableSeats(amount: Short){
        availableSeats = (availableSeats - amount).toShort()
    }

    fun increaseAvailableSeats(amount: Short){
        availableSeats = (availableSeats + amount).toShort()
    }

    companion object {
        fun fromDomain(cars: Collection<Car>): Collection<CarEntity> =
            cars.map {
                it.fromDomain()
            }

        private fun Car.fromDomain(): CarEntity =
            CarEntity(
                id = this.id.value,
                seats = this.seats,
                availableSeats = this.availableSeats
            )
    }
}
