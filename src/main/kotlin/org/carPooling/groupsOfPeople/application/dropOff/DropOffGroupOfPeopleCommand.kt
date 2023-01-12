package org.carPooling.groupsOfPeople.application.dropOff

import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId

data class DropOffGroupOfPeopleCommand(val groupOfPeopleId: Int) {
    fun toDomain(): GroupOfPeopleId = GroupOfPeopleId(groupOfPeopleId)
}