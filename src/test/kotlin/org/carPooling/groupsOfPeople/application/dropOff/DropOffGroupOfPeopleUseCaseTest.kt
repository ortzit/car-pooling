package org.carPooling.groupsOfPeople.application.dropOff

import com.nhaarman.mockitokotlin2.*
import org.carPooling.cars.domain.anyCarId
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue
import org.carPooling.groupsOfPeople.domain.dropOff.DropOffGroupOfPeopleService
import org.carPooling.shared.domain.GroupOfPeopleNotFoundException
import org.carPooling.shared.domain.anyGroupOfPeopleNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DropOffGroupOfPeopleUseCaseTest {
    private val service: DropOffGroupOfPeopleService = mock()
    private val useCase: DropOffGroupOfPeopleUseCase = DropOffGroupOfPeopleUseCase(service)

    @Test
    fun `should throw error when group of people is not found`() {
        // given
        val command = anyDropOffGroupOfPeopleCommand()
        whenever(service.dropOff(command.asGroupOfPeopleId()))
            .thenThrow(anyGroupOfPeopleNotFoundException())

        // expect
        assertThrows<GroupOfPeopleNotFoundException> { useCase.run(command) }
        verify(service, never()).tryToFillFreeSeats(any())
    }

    @Test
    fun `should not try to fill free seats when group of people has no car assigned`() {
        // given
        val command = anyDropOffGroupOfPeopleCommand()
        whenever(service.dropOff(command.asGroupOfPeopleId()))
            .thenReturn(null)

        // when
        useCase.run(command)

        // then
        verify(service, never()).tryToFillFreeSeats(any())
    }

    @Test
    fun `should try to fill free seats when group of people has car assigned`() {
        // given
        val command = anyDropOffGroupOfPeopleCommand()
        val carId = anyCarId()
        whenever(service.dropOff(command.asGroupOfPeopleId()))
            .thenReturn(carId)

        // when
        useCase.run(command)

        // then
        verify(service).tryToFillFreeSeats(carId)
    }

    private fun anyDropOffGroupOfPeopleCommand(): DropOffGroupOfPeopleCommand =
        DropOffGroupOfPeopleCommand(anyGroupOfPeopleIdValue())

    private fun DropOffGroupOfPeopleCommand.asGroupOfPeopleId(): GroupOfPeopleId =
        GroupOfPeopleId(this.groupOfPeopleId)
}