package org.carPooling.groupsOfPeople.application.dropOff

import org.carPooling.groupsOfPeople.domain.dropOff.DropOffGroupOfPeopleService
import org.carPooling.shared.application.CommandUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DropOffGroupOfPeopleUseCase @Autowired constructor(
    private val service: DropOffGroupOfPeopleService
) : CommandUseCase<DropOffGroupOfPeopleCommand> {
    override fun run(command: DropOffGroupOfPeopleCommand) {
        command.toDomain().let { groupOfPeopleId ->
            synchronized(service) {
                service.dropOff(groupOfPeopleId)?.let { leftCarId ->
                    service.tryToFillFreeSeats(leftCarId)
                }
            }
        }
    }
}