package org.carPooling.groupsOfPeople.domain

import org.carPooling.anyPositiveInt
import org.carPooling.anyPositiveShort

fun anyGroupOfPeople() =
    GroupOfPeople(
        id = anyGroupOfPeopleId(),
        people = anyPeople()
    )

fun anyGroupOfPeopleId(): GroupOfPeopleId = GroupOfPeopleId(anyGroupOfPeopleIdValue())

fun anyPeople(minValue: Short = MIN_PEOPLE_PER_GROUP): Short =
    anyPositiveShort(minValue = minValue, maxValue = MAX_PEOPLE_PER_GROUP)

fun anyGroupOfPeopleIdValue() = anyPositiveInt()

private const val MIN_PEOPLE_PER_GROUP: Short = 1
private const val MAX_PEOPLE_PER_GROUP: Short = 6