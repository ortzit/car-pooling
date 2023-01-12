package org.carPooling.groupsOfPeople.application.add

import com.nhaarman.mockitokotlin2.*
import org.carPooling.anyString
import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId
import org.carPooling.groupsOfPeople.domain.add.AddGroupOfPeopleService
import org.carPooling.groupsOfPeople.domain.add.AddGroupOfPeopleValidator
import org.carPooling.groupsOfPeople.domain.add.InvalidGroupOfPeopleSizeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddGroupOfPeopleUseCaseTest {
    private val validator: AddGroupOfPeopleValidator = mock()
    private val service: AddGroupOfPeopleService = mock()
    private val useCase: AddGroupOfPeopleUseCase = AddGroupOfPeopleUseCase(
        validator = validator,
        service = service,
    )

    @Test
    fun `should validate and add assigning car if possible`() {
        // given
        val command = anyAddGroupOfPeopleCommand()

        // when
        useCase.run(command)

        // then
        val expectedGroupOfPeople = command.asGroupOfPeople()
        verify(validator).check(expectedGroupOfPeople)
        verify(service).addAndAssignCarIfPossible(expectedGroupOfPeople)
    }

    @Test
    fun `should throw exception and avoid adding and assigning car to the group of people when validation fails`() {
        // given
        val command = anyAddGroupOfPeopleCommand()
        val expectedGroupOfPeople = command.asGroupOfPeople()
        whenever(validator.check(expectedGroupOfPeople))
            .thenThrow(anyInvalidGroupOfPeopleSizeException())

        // expect
        assertThrows<InvalidGroupOfPeopleSizeException> { useCase.run(command) }
        verify(service, never()).addAndAssignCarIfPossible(any())
    }

    private fun AddGroupOfPeopleCommand.asGroupOfPeople(): GroupOfPeople =
        GroupOfPeople(
            id = GroupOfPeopleId(this.id),
            people = people
        )

    private fun anyInvalidGroupOfPeopleSizeException(): InvalidGroupOfPeopleSizeException =
        InvalidGroupOfPeopleSizeException(anyString())
}