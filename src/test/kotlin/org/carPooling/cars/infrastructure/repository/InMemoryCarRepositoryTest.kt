package org.carPooling.cars.infrastructure.repository

import org.carPooling.anyNotEmptyListOf
import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.anyCar
import org.carPooling.cars.domain.anySeats
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryCarRepositoryTest {
    private val repository: InMemoryCarRepository = InMemoryCarRepository()

    @BeforeEach
    fun clear() {
        repository.clear()
    }

    @Test
    fun `should load all cars`() {
        // given
        val cars = anyNotEmptyListOf { anyCar() }

        // when
        repository.load(cars)
        val loadedCars = cars.map { repository.get(it.id)!! }

        // then
        assertEquals(cars, loadedCars)
    }

    @Test
    fun `should return no car result when cleared all saved data`() {
        // given
        val loadedCars = anyLoadedCars()

        // when
        repository.clear()
        val result = loadedCars.mapNotNull { repository.get(it.id) }

        // then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return car when finding car by less seats than available`() {
        // given
        val loadedCar = anyLoadedCar()

        // when
        val result = repository.findCarByGroupSize(loadedCar.availableSeats.dec())

        // then
        assertEquals(loadedCar, result)
    }

    @Test
    fun `should return car when finding car by equal seats than available`() {
        // given
        val loadedCar = anyLoadedCar()

        // when
        val result = repository.findCarByGroupSize(loadedCar.availableSeats)

        // then
        assertEquals(loadedCar, result)
    }

    @Test
    fun `should return null when finding car by more seats than available`() {
        // given
        val loadedCar = anyLoadedCar()
        val availableSeats = loadedCar.availableSeats.inc()

        // when
        val result = repository.findCarByGroupSize(availableSeats)

        // then
        assertNull(result)
    }

    @Test
    fun `should return null when finding car with no car loaded`() {
        // given
        val availableSeats = anySeats()

        // when
        val result = repository.findCarByGroupSize(availableSeats)

        // then
        assertNull(result)
    }

    @Test
    fun `should return null when when finding car with all cars full`() {
        // given
        val loadedCar = anyLoadedCar()
        repository.decreaseAvailableSeats(loadedCar.id, loadedCar.seats)

        // when
        val result = repository.findCarByGroupSize(loadedCar.availableSeats)

        // then
        assertNull(result)
    }

    @Test
    fun `should return the car with zero available seats when decreasing car's all available seats`() {
        // given
        val loadedCar = anyLoadedCar()

        // when
        repository.decreaseAvailableSeats(
            carId = loadedCar.id,
            value = loadedCar.availableSeats
        )
        val result = repository.get(loadedCar.id)!!.availableSeats

        // then
        assertEquals(0, result)
    }

    @Test
    fun `should return the car with updated available seats when increasing the car's available seats`() {
        // given
        val car = anyCar().copy(availableSeats = 0)
        val loadedCar = anyLoadedCar(car)
        val increaseSeats = anySeats()

        // when
        repository.increaseAvailableSeats(
            carId = loadedCar.id,
            value = increaseSeats
        )
        val result = repository.get(loadedCar.id)!!.availableSeats

        // then
        assertEquals(increaseSeats, result)
    }

    private fun anyLoadedCars(cars: Collection<Car> = listOf(anyCar())): Collection<Car> =
        cars.apply { repository.load(this) }

    private fun anyLoadedCar(car: Car = anyCar()): Car =
        car.apply { anyLoadedCars(listOf(this)) }
}