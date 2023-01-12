package org.carPooling.groupsOfPeople.application.locate

import org.carPooling.groupsOfPeople.domain.GroupOfPeopleId

data class LocateGroupOfPeopleQuery(val groupOfPeopleId: Int) {
    fun toDomain(): GroupOfPeopleId =
        GroupOfPeopleId(value = groupOfPeopleId)
}