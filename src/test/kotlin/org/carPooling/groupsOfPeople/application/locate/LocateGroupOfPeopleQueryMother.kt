package org.carPooling.groupsOfPeople.application.locate

import org.carPooling.groupsOfPeople.domain.anyGroupOfPeopleIdValue

fun anyLocateGroupOfPeopleQuery(): LocateGroupOfPeopleQuery = LocateGroupOfPeopleQuery(
    groupOfPeopleId = anyGroupOfPeopleIdValue()
)