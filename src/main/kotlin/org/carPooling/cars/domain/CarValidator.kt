package org.carPooling.cars.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CarValidator {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun check(cars: Collection<Car>) {
        cars.checkDuplicatedId()
        cars.forEach { checkSeatCount(it) }
    }

    private fun Collection<Car>.checkDuplicatedId() {
        this.distinctBy { it.id }.takeIf { carsWithUniqueIds -> carsWithUniqueIds.size != this.size }?.let {
            logger.info("Tried to load cars with duplicated ids")
            throw DuplicatedCarIdException("Duplicated 'id' field values found")
        }
    }

    private fun checkSeatCount(car: Car) {
        car.takeIf { it.seats < MIN_CAR_SEATS || it.seats > MAX_CAR_SEATS }?.let { invalidCar ->
            logger.info("Tried to add ${invalidCar.id} car with invalid seats count [${invalidCar.seats}]")
            throw InvalidNumberOfSeatsException(
                "[${car.id}] car seats value [${car.seats}] should be between $MIN_CAR_SEATS and $MAX_CAR_SEATS"
            )
        }
    }

    companion object {
        private const val MIN_CAR_SEATS: Short = 4
        private const val MAX_CAR_SEATS: Short = 6
    }
}