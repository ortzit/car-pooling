package org.carPooling.groupsOfPeople.application.locate

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.carPooling.cars.domain.Car
import org.carPooling.cars.domain.anyCar
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.locate.LocateGroupOfPeopleService
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.carPooling.shared.domain.anyGroupOfPeopleNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LocateGroupOfPeopleUseCaseTest {
    private val service: LocateGroupOfPeopleService = mock()
    private val useCase: LocateGroupOfPeopleUseCase = LocateGroupOfPeopleUseCase(service)

    @Test
    fun `should throw error when group of people does not exist`() {
        // given
        val query = anyLocateGroupOfPeopleQuery()
        whenever(service.getAssignedCar(query.asGroupOfPeopleId())).thenThrow(anyGroupOfPeopleNotFoundException())

        // expect
        assertThrows<GroupOfPeopleNotFoundException> { useCase.query(query) }
    }

    @Test
    fun `should return null when locating group of people with no car assigned`() {
        // given
        val query = anyLocateGroupOfPeopleQuery()
        whenever(service.getAssignedCar(query.asGroupOfPeopleId())).thenReturn(null)

        // when
        val result = useCase.query(query)

        // then
        assertNull(result)
    }

    @Test
    fun `should return the car when locating group of people with car assigned`() {
        // given
        val query = anyLocateGroupOfPeopleQuery()
        val car = anyCar()
        whenever(service.getAssignedCar(query.asGroupOfPeopleId())).thenReturn(car)

        // when
        val result = useCase.query(query)

        // then
        val expected = car.asLocateGroupOfPeopleResult()
        assertEquals(expected, result)
    }

    private fun LocateGroupOfPeopleQuery.asGroupOfPeopleId(): GroupOfPeopleId =
        GroupOfPeopleId(this.groupOfPeopleId)

    private fun Car.asLocateGroupOfPeopleResult(): LocateGroupOfPeopleResult =
        LocateGroupOfPeopleResult(
            id = this.id.value,
            seats = this.seats
        )
}