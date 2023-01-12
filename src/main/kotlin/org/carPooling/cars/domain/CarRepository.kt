package org.carPooling.cars.domain

import org.carPooling.shared.domain.CarPoolingRepository

interface CarRepository : CarPoolingRepository {
    fun load(cars: Collection<Car>)

    fun get(carId: CarId): Car?

    fun findCarByGroupSize(people: Short): Car?

    fun decreaseAvailableSeats(carId: CarId, value: Short)

    fun increaseAvailableSeats(carId: CarId, value: Short)
}