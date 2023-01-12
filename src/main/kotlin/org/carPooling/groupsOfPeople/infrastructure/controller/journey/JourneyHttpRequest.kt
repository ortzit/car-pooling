package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import kotlinx.serialization.Serializable
import org.carPooling.groupsOfPeople.application.add.AddGroupOfPeopleCommand

@Serializable
data class JourneyHttpRequest(val id: Int, val people: Short) {
    fun toCommand(): AddGroupOfPeopleCommand =
        AddGroupOfPeopleCommand(
            id = id,
            people = people
        )
}