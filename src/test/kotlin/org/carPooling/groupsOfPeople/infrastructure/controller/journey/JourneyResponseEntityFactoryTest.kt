package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.anyGenericException
import org.carPooling.anyString
import org.carPooling.groupsOfPeople.domain.add.GroupOfPeopleAlreadyExistsException
import org.carPooling.groupsOfPeople.domain.add.InvalidGroupOfPeopleSizeException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class JourneyResponseEntityFactoryTest {
    private val factory: JourneyResponseEntityFactory = JourneyResponseEntityFactory(
        listOf(
            JourneyOkResponseMapper(),
            JourneyGroupOfPeopleAlreadyExistsExceptionMapper(),
            JourneyInvalidGroupOfPeopleSizeExceptionMapper(),
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
    fun `should return conflict response when receiving group of people already exists exception`() {
        // given
        val exception = anyGroupOfPeopleAlreadyExistsException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.CONFLICT).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return bad request response when receiving invalid group of people size exception`() {
        // given
        val exception = anyInvalidGroupOfPeopleSizeException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return internal server error when unknown request response when receiving unknown result`() {
        // given
        val exception = anyGenericException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("")
        assertEquals(expected, result)
    }

    private fun anyGroupOfPeopleAlreadyExistsException(): GroupOfPeopleAlreadyExistsException =
        GroupOfPeopleAlreadyExistsException(anyString())

    private fun anyInvalidGroupOfPeopleSizeException(): InvalidGroupOfPeopleSizeException =
        InvalidGroupOfPeopleSizeException(anyString())
}