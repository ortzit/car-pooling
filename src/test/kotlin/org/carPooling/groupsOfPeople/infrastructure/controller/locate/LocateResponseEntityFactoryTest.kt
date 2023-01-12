package org.carPooling.groupsOfPeople.infrastructure.controller.locate

import org.carPooling.anyGenericException
import org.carPooling.anyString
import org.carPooling.cars.domain.anySeats
import org.carPooling.groupsOfPeople.application.locate.LocateGroupOfPeopleResult
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class LocateResponseEntityFactoryTest {
    private val factory: LocateResponseEntityFactory = LocateResponseEntityFactory(
        listOf(
            LocateNoContentResponseMapper(),
            LocateOkResponseMapper(),
            LocateGroupOfPeopleNotFoundExceptionMapper(),
        )
    )

    @Test
    fun `should return no content response when receiving null`() {
        // given
        val nullResponse = null

        // when
        val result = factory.build(nullResponse)

        // then
        val expected = ResponseEntity.status(HttpStatus.NO_CONTENT).body("")
        assertEquals(expected, result)
    }

    @Test
    fun `should return ok response when receiving locate group of people response`() {
        // given
        val response = anyLocateGroupOfPeopleResult()

        // when
        val result = factory.build(response)

        // then
        val expected = ResponseEntity.status(HttpStatus.OK).body(response.asLocateResponse())
        assertEquals(expected, result)
    }

    @Test
    fun `should return not found response when receiving group of people not found exception`() {
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

    private fun anyLocateGroupOfPeopleResult(): LocateGroupOfPeopleResult  =
        LocateGroupOfPeopleResult(
            id = anyGroupOfPeopleIdValue(),
            seats = anySeats()
        )

    private fun LocateGroupOfPeopleResult.asLocateResponse(): LocateResponse =
        LocateResponse(
            id = this.id,
            seats = this.seats,
        )

    private fun anyGroupOfPeopleNotFoundException(): GroupOfPeopleNotFoundException =
        GroupOfPeopleNotFoundException(anyString())
}