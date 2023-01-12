package org.carPooling.groupsOfPeople.application.add

import org.carPooling.cars.domain.anySeats
import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue

fun anyAddGroupOfPeopleCommand(): AddGroupOfPeopleCommand =
    AddGroupOfPeopleCommand(
        id = anyGroupOfPeopleIdValue(),
        people = anySeats()
    )