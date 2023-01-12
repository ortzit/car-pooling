package org.carPooling.cars.domain

import org.carPooling.anyNotEmptyListOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CarValidatorTest {
    private val validator: CarValidator = CarValidator()

    @Test
    fun `should throw no exception when checking valid cars`() {
        // given
        val cars = anyNotEmptyListOf { anyCar() }

        // expect
        validator.check(cars)
    }

    @Test
    fun `should throw error when validating duplicated ids`() {
        // given
        val duplicatedId = anyCarId()
        val cars = listOf(
            anyCar().copy(id = duplicatedId),
            anyCar().copy(id = duplicatedId),
        )

        // expect
        assertThrows<DuplicatedCarIdException> {
            validator.check(cars)
        }
    }

    @Test
    fun `should throw error when validating car with seats below minimum`() {
        // given
        val cars = listOf(anyCar().copy(seats = MIN_CAR_SEATS.dec()))

        // expect
        assertThrows<InvalidNumberOfSeatsException> {
            validator.check(cars)
        }
    }

    @Test
    fun `should throw error when validating with seats above maximum`() {
        // given
        val cars = listOf(anyCar().copy(seats = MAX_CAR_SEATS.inc()))

        // expect
        assertThrows<InvalidNumberOfSeatsException> {
            validator.check(cars)
        }
    }

    companion object {
        private const val MIN_CAR_SEATS: Short = 4
        private const val MAX_CAR_SEATS: Short = 6
    }
}