package org.carPooling.cars.application.loadCars

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.carPooling.cars.domain.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.junit.jupiter.api.assertThrows

class LoadCarsUseCaseTest {
    private val validator: CarValidator = mock()
    private val carService: CarService = mock()
    private val useCase: LoadCarsUseCase = LoadCarsUseCase(
        validator = validator,
        carService = carService
    )

    @Test
    fun `should call validator and service load when valid load cars command sent`() {
        // given
        val loadCarsCommand = anyLoadCarsCommand()

        // when
        useCase.run(loadCarsCommand)

        // then
        val expectedCars = loadCarsCommand.toCars()
        Mockito.verify(validator).check(expectedCars)
        Mockito.verify(carService).load(expectedCars)
    }

    @Test
    fun `should not call service load when validator check throws exception`() {
        // given
        val loadCarsCommand = anyLoadCarsCommand()
        val expectedCars = loadCarsCommand.toCars()

        whenever(validator.check(expectedCars))
            .thenThrow(anyDuplicatedCarIdException())

        // expect
        assertThrows<DuplicatedCarIdException> {
            useCase.run(loadCarsCommand)
        }
        Mockito.verify(carService, Mockito.never()).load(expectedCars)
    }

    private fun LoadCarsCommand.toCars(): Collection<Car> =
        this.cars.map { carCommand ->
            Car(
                id = CarId(carCommand.id),
                seats = carCommand.seats,
                availableSeats = carCommand.seats,
            )
        }
}