package org.carPooling.groupsOfPeople.infrastructure.controller.journey

import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue
import org.carPooling.groupsOfPeople.domain.anyPeople

fun anyJourneyHttpRequest(): JourneyHttpRequest =
    JourneyHttpRequest(
        id = anyGroupOfPeopleIdValue(),
        people = anyPeople()
    )