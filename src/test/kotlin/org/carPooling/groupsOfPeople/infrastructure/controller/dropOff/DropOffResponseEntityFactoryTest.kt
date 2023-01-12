package org.carPooling.groupsOfPeople.infrastructure.controller.dropOff

import org.carPooling.anyGenericException
import org.carPooling.shared.domain.anyGroupOfPeopleNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class DropOffResponseEntityFactoryTest {
    private val factory: DropOffResponseEntityFactory = DropOffResponseEntityFactory(
        listOf(
            DropOffOkResponseMapper(),
            DropOffGroupOfPeopleNotFoundExceptionMapper(),
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
    fun `should return not found when receiving group of people not found exception`() {
        // given
        val exception = anyGroupOfPeopleNotFoundException()

        // when
        val result = factory.build(exception)

        // then
        val expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("")
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
}