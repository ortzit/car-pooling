package org.carPooling.groupsOfPeople.application.add

import org.carPooling.groupsOfPeople.domain.GroupOfPeople
import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId

data class AddGroupOfPeopleCommand(val id: Int, val people: Short) {
    fun toDomain(): GroupOfPeople =
        GroupOfPeople(
            id = GroupOfPeopleId(id),
            people = people
        )
}