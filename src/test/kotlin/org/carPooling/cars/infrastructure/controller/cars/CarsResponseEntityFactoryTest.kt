package org.carPooling.cars.infrastructure.controller.cars

import org.carPooling.anyGenericException
import org.carPooling.anyString
import org.carPooling.cars.domain.InvalidNumberOfSeatsException
import org.carPooling.cars.domain.anyDuplicatedCarIdException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CarsResponseEntityFactoryTest {
    private val factory: CarsResponseEntityFactory = CarsResponseEntityFactory(
        listOf(
            CarsOkResponseMapper(),
            CarsDuplicatedCarIdExceptionMapper(),
            CarsInvalidNumberOfSeatsExceptionMapper()
        )
    )

    @Test
    fun `should return ok response when receiving nothing`() {
        // given
        val nothing = Unit

        // when
        val result = factory.build(nothing)

        // then
        val expected = ResponseEntity.status(HttpStatus.OK).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return bad request response when receiving duplicated car id exception`() {
        // given
        val exception = anyDuplicatedCarIdException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return bad request response when receiving invalid number of seats exception`() {
        // given
        val exception = anyInvalidNumberOfSeatsException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return not implemented error when receiving unknown result`() {
        // given
        val exception = anyGenericException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("")
        assertEquals(expected, result)
    }

    private fun anyInvalidNumberOfSeatsException(): InvalidNumberOfSeatsException =
        InvalidNumberOfSeatsException(anyString())
}