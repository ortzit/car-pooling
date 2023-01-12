package org.carPooling.groupsOfPeople.application.add

import org.carPooling.groupsOfPeople.domain.add.AddGroupOfPeopleService
import org.carPooling.groupsOfPeople.domain.add.AddGroupOfPeopleValidator
import org.carPooling.shared.application.CommandUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddGroupOfPeopleUseCase @Autowired constructor(
    private val validator: AddGroupOfPeopleValidator,
    private val service: AddGroupOfPeopleService
) : CommandUseCase<AddGroupOfPeopleCommand> {
    override fun run(command: AddGroupOfPeopleCommand) {
        command.toDomain().let { groupOfPeople ->
            validator.check(groupOfPeople)
            synchronized(service) {
                service.addAndAssignCarIfPossible(groupOfPeople)
            }
        }
    }
}